<?php

class estate_panel_owner extends SubPanelsDwidget {

    private $model;

    public function __construct(){
    }

    public function setModel($obj){
	$this->model = $obj;
    }

    public function getHTML(){
	return $this->load('tmpl/widgets/estate/panelsview/estatepanelguestowner.html');
    }

    protected function genSubWidget($dwname){
	if( $dwname == 'type' ){
	    return $this->model['type']=='give'?'Сдам':'Сниму';
	} else if( $dwname == 'obj' ) {
	    return $this->model['obj'];
	} else if( $dwname == 'date' ) {
	    return $this->model['date'];
	} else if( $dwname == 'floor' ) {
	    return $this->model['floor'];
	} else if( $dwname == 'space' ) {
	    return $this->model['space'];
	} else if( $dwname == 'addr' ) {
	    return $this->model['addr'];
	} else if( $dwname == 'price' ) {
	    return $this->model['cost'];
	} else if( $dwname == 'phones' ) {
	    return '';
	} else if( $dwname == 'info' ) {
	    return $this->model['info'];
	} else return '';
    }

}

?>
