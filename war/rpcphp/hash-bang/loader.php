<?php

require_once 'dwidget.php';

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
//		'/<div[\w\d\s="\'_]*id=["\']dvijokw["\'][\w\d\s="\'_]*name=["\']([\w\d_]+)["\'][\w\d\s="\'_]*>[^<]*<\/div>/',
		'/<div[\w\d\s-="\'_]*data-dvijok-dwidget-name=["\']([\w\d_]+)["\'][\w\d\s-="\'_]*>[^<]*<\/div>/',
		array($this, 'load_dwidget'),
		$cont);
	
//	    $cont = preg_replace_callback(
//		'/<div[\w\d\s="\'_]*name=["\']([\w\d_]+)["\'][\w\d\s="\'_]*id=["\']dvijokw["\'][\w\d\s="\'_]*>[^<]*<\/div>/',
//		array($this, 'load_dwidget'),
//		$cont);
	
	} while($this->found == true);
	
	return $cont;
    }

    public function load_dwidget($matches){
	$this->found = true;
	$name = $matches[1];
	$div = str_replace('data-dvijok-dwidget-name', 'data-dvijok-dwidget-name_', $matches[0]);
	if( class_exists($name) ){
	    $dwidget = new $name();
		$dwidget->setHTMLTagAttributes($this->readAttributes($matches[0]));
	    return $dwidget->getHTML();
	} else {
error_log(print_r($matches, true));
		return $div;
	}
    }
    
    private function readAttributes($htmltag){
    	
    	preg_match_all(
    	"/[\s<>]*([^\s<>]*)=['\"]*([^\s<>'\"]*)['\"]*[\s<>]*/",
    	$htmltag,
    	$out, PREG_PATTERN_ORDER);
    	
    	$attrs = array();
		for( $i=0; $i<count($out[1]); $i++ ){
			$attrs[$out[1][$i]] = $out[2][$i]; 
		}
				
		return $attrs;
		
    }

}

?>
