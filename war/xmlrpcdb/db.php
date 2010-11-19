<?php

//    dvijok - cms written in gwt
//    Copyright (C) 2010  Pechenko Anton Vladimirovich aka Parilo
//    mailto: forpost78 at gmail dot com
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>

/*
 * db.php - database.
 * types:
 *  - int
 *  - str
 *  - func
 *  - double
 */
 
 class database {
 	
 	//database id
	protected $dbid = null;
	
	protected $dbhost = "";
	protected $dbuser = "";
	protected $dbpass = "";
	protected $dbname = "";
	
	private $lastsql = "";
	private $mysql_result = null;

	protected $dberr = "";

	public function __construct($dbhost, $dbuser, $dbpass, $dbname){
		
		$this->dbhost = $dbhost;
		$this->dbuser = $dbuser;
		$this->dbpass = $dbpass;
		$this->dbname = $dbname;
		
		$this->dbid = mysql_connect($dbhost, $dbuser, $dbpass, true);
		mysql_set_charset("utf8", $this->dbid);
		if( $this->dbid != 0 ){
			$ret = mysql_select_db($dbname, $this->dbid);
			if( $ret == 0 ){
				print "db.php: __constructor(): cannot select db\n";
			}
		} else {
			print "db.php: __constructor(): cannot connect to mysql\n";
		}
	}

	public function getDBName(){
		return $this->dbname;
	}
	
	protected function getLastSql(){
		return $this->lastsql;
	}

  	public function getLastError(){
		return $this->dberr;
	}
	
	protected function setLastError($err){
		$this->dberr = $err;
	}

	private function where_parse($where, $types){
		$wherestr = "";
		foreach( $where as $key => $val ){
			$wherestr .= " AND ".mysql_real_escape_string($key);
			
			if( $types[$key] == 'str' ){
				$wherestr .= " = '".mysql_real_escape_string($val)."'";
			} else if( $types[$key] == 'between' ) {
				$wherestr .= ' BETWEEN '.mysql_real_escape_string($val['a']).' AND '.mysql_real_escape_string($val['b']);
			} else if( $types[$key] == 'between_noescape' ) {
				$wherestr .= ' BETWEEN '.$val['a'].' AND '.$val['b'];
			} else {
				$wherestr .= ' = '.mysql_real_escape_string($val);
			} 
		}
		
		return $wherestr;
	}
	
	private function join_parse($join){
		$joinstr = '';
		foreach( $join as $table => $on ){
			$joinstr .= " LEFT JOIN ".mysql_real_escape_string($table)." ON ".$on['l'].' = '.$on['r'];
			$i=0;
			while( isset($on["add$i"]) ){
				$op = $on["add$i"];
				if( $op['noescape'] == 'true' ){
					$joinstr .= " {$op['oper']} {$op['l']} = {$op['r']}";
				} else {
					$joinstr .= ' '.mysql_real_escape_string($op['oper']).' '.mysql_real_escape_string($op['l']).' = '.mysql_real_escape_string($op['r']);
				}
				$i++;
			}
		}
		
		return $joinstr;
	}
		
 	private function set_parse($setobj, $types){
		$set = '';
		foreach( $setobj as $key => $val ){
			$set .= ",".mysql_real_escape_string($key)." = ";
			
			if( $types[$key] == 'str' ){
				$set .= "'".mysql_real_escape_string($val)."'";
			} else {
				$set .= mysql_real_escape_string($val);
			} 
		}
		return $set;
 	}
 	
 	private function cols_vals_parse($obj, $types){
		$cols = "";
		$vals = "";
		foreach( $types as $key => $val ){
		$cols .= ",".mysql_real_escape_string($key);
			
			if( $val == 'str' ){
				$vals .= ",'".mysql_real_escape_string($obj[$key])."'";
			} else if( $val == 'int' ) {
				$vals .= ','.($obj[$key]==''?0:mysql_real_escape_string($obj[$key]));
			} else {
				$vals .= ",".mysql_real_escape_string($obj[$key]);
			} 
			
		}
		
		return array( 'cols' => $cols, 'vals' => $vals );
 	}

 	private function cols_vals_bulk_parse($objs, $types){
		$cols = "";
		$allvals = "";
		foreach( $types as $key => $val )
			$cols .= ",".mysql_real_escape_string($key);
			
		for( $i=0; $i<count($objs); $i++ ){
			
			$vals = "";
			foreach($types as $key => $val ){
				if( $val == 'str' ){
					$vals .= ",'".mysql_real_escape_string($objs[$i][$key])."'";
				} else {
					$vals .= ",".mysql_real_escape_string($objs[$i][$key]);
				}
			}
			$allvals .= ",(".substr($vals,1).")";
			
		}
		
		return array( 'cols' => substr($cols,1), 'vals' => substr($allvals,1) );
 	}
 	
 	private function cols_parse($cols){
 		$colsstr = '';
 		for($i=0; $i<count($cols); $i++){ $colsstr .= ','.$cols[$i]; }
 		return substr($colsstr, 1);
 	}
 	
	private function order_parse($ord){
		$order = '';
		foreach( $ord as $col => $type ){
			$order .= ",".mysql_real_escape_string($col)." ".mysql_real_escape_string($type);
		}
		
		return "ORDER BY ".substr($order,1);
	}
	
	private function groupby_parse($grp){
 		$groupby = '';
 		for($i=0; $i<count($grp); $i++){ $groupby .= ','.mysql_real_escape_string($grp[$i]); }
 		return " GROUP BY ".substr($groupby, 1);
	}
	
	private function limit_parse($lmt){
		return "LIMIT ".mysql_real_escape_string($lmt['offset']).",".mysql_real_escape_string($lmt['limit']);
	}
	
	private function create_select_join($table, $join, $where, $types, $cols = array('*'), $order=null, $groupby=null, $limit=null){
		$where = $this->where_parse($where, $types);
		$join = $join==null?'':$this->join_parse($join);
		$cols = $cols==null?'*':$this->cols_parse($cols);
		$order = $order==null?'':$this->order_parse($order);
		$groupby = $groupby==null?'':$this->groupby_parse($groupby);
		$limit = $limit==null?'':$this->limit_parse($limit);
		return 'SELECT '.$cols.' FROM '.mysql_real_escape_string($table).' '.$join.' WHERE '.substr($where,5)."$groupby $order $limit";
 	}
 	
 	protected function select_query($query){
		$ret = $this->select_query_not_fetch($query);
		if( $ret === false ) return false;
		else return mysql_fetch_assoc($this->mysql_result);
 	}

 	protected function select_query_not_fetch($query){
		$this->mysql_result = mysql_query($query, $this->dbid);
 		$this->lastsql = $query;
		$ra = mysql_affected_rows($this->dbid);
		if( $ra == -1 ){
			$this->dberr = "db.php: select_query: ".mysql_error();
			return false;
		} elseif( $ra == 0 ) {
			$this->dberr = '0'; // no rows was affected
			return false;
		}
		return true;
 	}
 	
 	protected function select_next(){
 		if( $this->mysql_result === false ) return false;
 		else return mysql_fetch_assoc($this->mysql_result);
 	}

 	protected function select($table, $cols, $join, $where, $types){
 		$sql = $this->create_select_join($table, $join, $where, $types, $cols);
// 		echo $sql;
 		return $this->select_query($sql);
 	}
 	
 	protected function select_not_fetch($table, $cols, $join, $where, $types, $order=null, $groupby=null, $limit=null){
 		$sql = $this->create_select_join($table, $join, $where, $types, $cols, $order, $groupby, $limit);
// 		echo $sql;
 		return $this->select_query_not_fetch($sql);
 	}
 	
 	protected function select_bulk($table, $cols, $join, $where, $types, $order=null, $groupby=null, $limit=null){
		$ret = $this->select_not_fetch(
				$table,
				$cols,
				$join,
				$where,
				$types,
				$order,
				$groupby,
				$limit
			);
		if( $ret === false ) return $this->ret(false,'notreg');
		
		$i=0;
		while( ($d = $this->select_next()) !== false ){
			$bulk[$i++] = $d;
		}
		
		return $bulk;
 	}

	protected function select_table($tblname){
		return $this->select_bulk(
				$tblname,
				array('*'),
				null,
				array( '1' => 1 ),
				array( '1' => 'int' )
			);
	}

	protected function select_table_cols($tblname, $cols){
		return $this->select_bulk(
				$tblname,
				$cols,
				null,
				array( '1' => 1 ),
				array( '1' => 'int' )
			);
	}
 	
 	protected function insert_query($query){
		$ret = mysql_query($query, $this->dbid);
 		$this->lastsql = $query;
		$ra = mysql_affected_rows($this->dbid);
		if( $ra == -1 ){
			$this->dberr = "db.php: insert_query: ".mysql_error();
			return false;
		} elseif( $ra == 0 ) {
			$this->dberr = '0'; // no rows was affected
			return false;
		}
		return mysql_insert_id($this->dbid);
 	}
 	
 	protected function insert_get_query($table, $obj, $types){
 		$cv = $this->cols_vals_parse($obj, $types); 
		$cols = $cv['cols'];
		$vals = $cv['vals'];
		
		return "INSERT INTO ".mysql_real_escape_string($table).'('.substr($cols,1).') VALUES ('.substr($vals,1).')';
 	}
 	
 	protected function insert_get_query_bulk($table, $objs, $types){
 		$cv = $this->cols_vals_parse($obj, $types); 
		$cols = $cv['cols'];
		$vals = $cv['vals'];
		
		return "INSERT INTO ".mysql_real_escape_string($table).'('.$cols.') VALUES '.$vals;
 	}
 	
	protected function insert($table, $obj, $types){
		$sql = $this->insert_get_query($table, $obj, $types);
//		echo $sql."\n";
		return $this->insert_query($sql);
	}
	
	protected function insert_bulk($table, $objs, $types){
		$sql = $this->insert_get_query_bulk($table, $obj, $types);
//		echo $sql;
		return $this->insert_query($sql);
	}
	
	protected function update_query($query){
		$ret = mysql_query($query, $this->dbid);
 		$this->lastsql = $query;
		$ra = mysql_affected_rows($this->dbid);
		if( $ra == -1 ){
			$this->dberr = "db.php: update_query: ".mysql_error();
			return false;
		} elseif( $ra == 0 ) {
			$this->dberr = '0'; // no rows was affected
			return false;
		}
		return true;
 	}
 	
 	protected function update_get_query($table, $obj, $types, $where, $wheretypes){
		$set = $this->set_parse($obj, $types);
		$wherestr = $this->where_parse($where, $wheretypes);
		return 'UPDATE '.mysql_real_escape_string($table).' SET '.substr($set,1).' WHERE '.substr($wherestr,4);
 	}
	
 	protected function update_join_get_query($table, $join, $obj, $types, $where, $wheretypes){
 		$set = $this->set_parse($obj, $types);
 		$join = $join==null?'':$this->join_parse($join);
 		$wherestr = $this->where_parse($where, $wheretypes);
 		$sql = 'UPDATE '.mysql_real_escape_string($table).' '.$join.' SET '.substr($set,1).' WHERE '.substr($wherestr,5);
 		return $sql;
 	}
 	
 	protected function update($table, $join, $obj, $types, $where, $wheretypes){
		if( count($obj) == 0 ){ $this->dberr=0; return false; }
		$sql = $this->update_join_get_query($table, $join, $obj, $types, $where, $wheretypes);
//		echo $sql;
 		return $this->update_query($sql);
 	}
 	
 	protected function delete_query($query){
		$ret = mysql_query($query, $this->dbid);
 		$this->lastsql = $query;
		$ra = mysql_affected_rows($this->dbid);
		if( $ra == -1 ){
			$this->dberr = "db.php: delete_query: ".mysql_error();
			return false;
		} elseif( $ra == 0 ) {
			$this->dberr = '0'; // no rows was affected
			return false;
		}
		return true;
 	}
 	
 	protected function delete_get_query($table, $where, $wheretypes){
		$wherestr = $this->where_parse($where, $wheretypes);
		return 'DELETE FROM '.mysql_real_escape_string($table).' WHERE '.substr($wherestr,4);
 	}
 	
	protected function delete($table, $where, $wheretypes){
		$sql = $this->delete_get_query($table, $where, $wheretypes);
//		echo $sql;
		return $this->delete_query($sql);
	}

 }
 
?>
