<?php

function objCmp($obj1, $obj2){
    $d1 = date_create_from_format('Y-m-d H:i:s', $obj1['date']);
    $d2 = date_create_from_format('Y-m-d H:i:s', $obj2['date']);
    return $d1 < $d2;
}

class estate_controller extends SubPanelsDwidget {

    private $objDict = array();

    public function __construct(){

	$this->objDict["room"] = "комната";
	$this->objDict["pension"] = "пансионат";
	$this->objDict["studio"] = "студия";
	$this->objDict["1r"] = "1 комнатная";
	$this->objDict["2r"] = "2 комнатная";
	$this->objDict["3r"] = "3 комнатная";
	$this->objDict["4r"] = "4 комнатная";
	$this->objDict["5r"] = "5 комнатная";
	$this->objDict["6r"] = "6 комнатная";
	$this->objDict["multi"] = "многокомнатная";
	$this->objDict["byday"] = "по суточно";
	$this->objDict["house"] = "частный дом";
	$this->objDict["cottage"] = "коттедж";
	$this->objDict["outdoor"] = "дача";
	$this->objDict["land"] = "земельный участок";
	$this->objDict["garage"] = "гараж";
	$this->objDict["other"] = "другое";

    }

    public function getHTML(){
	return $this->load('tmpl/widgets/estate/estate.html');
    }

    private function giveowner(){
	global $state;
	$estate = $state['estate'];
	$db = $state['db'];
	$sess = $state['sess'];
	$user = $state['user'];
	$dict = $this->objDict;

	$objs = $estate->getEstateOwnerGive('', $user, $sess, $db);
	$objs = $objs['objs']['ads'];
	if( isset($objs['_isarr']) ) unset($objs['_isarr']);
	usort($objs, 'objCmp');

	$out = '';

	foreach( $objs as $obj ){
	    if( isset($obj['date']) ){
		$obj['obj'] = $dict[$obj['obj']];
		$p = new estate_panel_owner();
		$p->setModel($obj);
		$out .= $p->getHTML();
	    }
	}

	return $out;
    }

    private function giveprobably(){
	global $state;
	$estate = $state['estate'];
	$db = $state['db'];
	$sess = $state['sess'];
	$user = $state['user'];
	$dict = $this->objDict;

	$objs = $estate->getEstateProbablyGive('', $user, $sess, $db);
	$objs = $objs['objs']['ads'];
	if( isset($objs['_isarr']) ) unset($objs['_isarr']);
	usort($objs, 'objCmp');

	$out = '';

	foreach( $objs as $obj ){
	    if( isset($obj['date']) ){
		$obj['obj'] = $dict[$obj['obj']];
		$p = new estate_panel_probably();
		$p->setModel($obj);
		$out .= $p->getHTML();
	    }
	}

	return $out;
    }

    private function take(){
	global $state;
	$estate = $state['estate'];
	$db = $state['db'];
	$sess = $state['sess'];
	$user = $state['user'];
	$dict = $this->objDict;

	$objs = $estate->getEstateTake('', $user, $sess, $db);
	$objs = $objs['objs']['ads'];
	if( isset($objs['_isarr']) ) unset($objs['_isarr']);
	usort($objs, 'objCmp');

	$out = '';

	foreach( $objs as $obj ){
	    if( isset($obj['date']) ){
		$obj['obj'] = $dict[$obj['obj']];
		$p = new estate_panel();
		$p->setModel($obj);
		$out .= $p->getHTML();
	    }
	}

	return $out;
    }

    protected function genSubWidget($dwname){

	global $state;
	if( $state['hashtoken'] == 'take' ){
	    return $this->take();
/*	} else if( $state['hashtoken'] == 'give2' ) {
	    return $this->give2();*/
	} else {
	    return $this->giveowner().$this->giveprobably();
	}

    }

}

?>
