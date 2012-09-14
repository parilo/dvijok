<?php

class estate_filter extends SubPanelsDwidget {

    public function getHTML(){
	return $this->load('tmpl/widgets/estate/estatefilter.html');
    }

    private function type(){
	return '<div class="gwt-Label large button dropdown" style="width: 140px; "><span class="gwt-InlineHTML">cдам</span><ul class="no-hover" style="top: 49px; "><li><a class="gwt-Anchor" href="/#!take">cниму</a></li></ul></div>';
    }

    private function obj(){
	return '<div class="gwt-Label large button dropdown" style="width: 300px; "><span class="gwt-InlineHTML">все</span><ul class="no-hover" style="top: 49px; "><li><a class="gwt-Anchor" href="javascript:;">комната</a></li><li><a class="gwt-Anchor" href="javascript:;">пансионат</a></li><li><a class="gwt-Anchor" href="javascript:;">студия</a></li><li><a class="gwt-Anchor" href="javascript:;">1 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">2 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">3 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">4 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">5 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">6 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">многокомнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">по суточно</a></li><li><a class="gwt-Anchor" href="javascript:;">частный дом</a></li><li><a class="gwt-Anchor" href="javascript:;">коттедж</a></li><li><a class="gwt-Anchor" href="javascript:;">дача</a></li><li><a class="gwt-Anchor" href="javascript:;">земельный участок</a></li><li><a class="gwt-Anchor" href="javascript:;">гараж</a></li><li><a class="gwt-Anchor" href="javascript:;">другое</a></li></ul></div>';
    }

    private function view(){
	return '<div class="gwt-Label large button dropdown" style="width: 176px; "><span class="gwt-InlineHTML">плитка</span><ul class="no-hover" style="top: 49px; "><li><a class="gwt-Anchor" href="javascript:;">табилца</a></li></ul></div>';
    }

    protected function genSubWidget($dwname){
	if( $dwname == 'object' ) return $this->obj();
	if( $dwname == 'view' ) return $this->view();
	if( $dwname == 'type' ) return $this->type();
	else return '';
    }

}

?>
