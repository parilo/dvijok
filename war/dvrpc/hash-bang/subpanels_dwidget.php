<?php

abstract class SubPanelsDwidget {

    private $found;

    public function __construct(){
    }

    protected function load($tmpl){
	global $config;
	$cont = file_get_contents($config['sitedir'].$tmpl);
	do{
	    $this->found = false;
	    $cont = preg_replace_callback(
		'/<div[^<]*id=["\']?dw["\']?[^<]*dwname=["\']([\w\d_]+)["\'][^<]*>[^<]*<\/div>/',
		array($this, 'load_subpanel'),
		$cont);
	
	    $cont = preg_replace_callback(
		'/<div[^<]*dwname=["\']([\w\d_]+)["\'][^<]*id=["\']?dw["\']?[^<]*>[^<]*<\/div>/',
		array($this, 'load_subpanel'),
		$cont);
	
	} while($this->found == true);
	
	return $cont;
    }

    abstract protected function genSubWidget($dwname);

    protected function load_subpanel($matches){
	$this->found = true;
	$name = $matches[1];
	$div = str_replace('dw', 'dw_', $matches[0]);
	return preg_replace('/>[^<]*</', '>'.$this->genSubWidget($name).'<', $div);
    }

}

?>
