<?php

class estate_label_givetake extends SubPanelsDwidget {

    public function getHTML(){
	global $state;
	if( $state['hashtoken'] == 'take' ){
	    return $this->load('tmpl/widgets/estate/estatelabeltake.html');
	} else {
	    return $this->load('tmpl/widgets/estate/estatelabelgive.html');
	}
    }

    protected function genSubWidget($dwname){
	return '';
    }

}

?>
