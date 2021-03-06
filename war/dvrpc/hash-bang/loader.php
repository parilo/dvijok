<?php

require_once 'subpanels_dwidget.php';

class Loader {

    private $found;

    public function __construct(){

	$paths = explode(PATH_SEPARATOR, get_include_path());
	foreach($paths as $p) {
	    $dirpath = $p.DIRECTORY_SEPARATOR.'hash-bang/dwidgets/';
	    if( is_dir($dirpath) ) if( $dh = opendir($dirpath) ){
	    
		while (($file = readdir($dh)) !== false) {
		    if( strEndsWith($file, 'php') ) require_once($dirpath.$file);
		}
		closedir($dh);

	    }
	}

    }

    public function load($page){
	global $config;
	$cont = file_get_contents($config['sitedir'].$page);
	$dws = array();
	
	do{
	    $this->found = false;
	    $cont = preg_replace_callback(
		'/<div[\w\d\s="\'_]*id=["\']dvijokw["\'][\w\d\s="\'_]*name=["\']([\w\d_]+)["\'][\w\d\s="\'_]*>[^<]*<\/div>/',
		array($this, 'load_dwidget'),
		$cont);
	
	    $cont = preg_replace_callback(
		'/<div[\w\d\s="\'_]*name=["\']([\w\d_]+)["\'][\w\d\s="\'_]*id=["\']dvijokw["\'][\w\d\s="\'_]*>[^<]*<\/div>/',
		array($this, 'load_dwidget'),
		$cont);
	
	} while($this->found == true);
	
	return $cont;
    }

    public function load_dwidget($matches){
	$this->found = true;
	$name = $matches[1];
	$div = str_replace('dvijokw', 'dvijokw_', $matches[0]);
	if( class_exists($name) ){
	    $dwidget = new $name();
	    return preg_replace('/>[^<]*</', '>'.$dwidget->getHTML().'<', $div);
	} else return $div;
    }

}

?>
