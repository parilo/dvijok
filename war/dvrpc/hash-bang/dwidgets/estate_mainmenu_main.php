<?php

class estate_mainmenu_main extends SubPanelsDwidget {

    public function getHTML(){
	return $this->load('tmpl/widgets/estate/menu/menuguest.html');
    }

    protected function genSubWidget($dwname){
	return '';
    }

}

?>
