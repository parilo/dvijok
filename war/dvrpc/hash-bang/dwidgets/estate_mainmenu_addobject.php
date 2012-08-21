<?php

class estate_mainmenu_addobject extends SubPanelsDwidget {

    public function getHTML(){
	return $this->load('tmpl/widgets/estate/menu/menuguestaddobj.html');
    }

    protected function genSubWidget($dwname){
	return '';
    }

}

?>
