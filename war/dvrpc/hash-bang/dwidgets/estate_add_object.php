<?php

class estate_add_object extends SubPanelsDwidget {

    public function getHTML(){
	return '<div id="dvijokw_" name="estate_add_object"><div><div class="tmplcont">  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Тип объявления:</label>
    </div>
    <div class="dwformfieldcont seven mobile-three columns" id="dw_" dwname="type"><div style="display: inline; "><div class="gwt-Label button dropdown"><span class="gwt-InlineHTML">Сдать</span><ul class="no-hover" style="top: 36px; "><li><a class="gwt-Anchor" href="javascript:;">Снять</a></li></ul></div></div></div>
  </div>

  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Объект:</label>
    </div>
    <div class="dwformfieldcont seven mobile-three columns" id="dw_" dwname="obj"><div style="display: inline; "><div class="gwt-Label button dropdown"><span class="gwt-InlineHTML">Комната</span><ul class="no-hover" style="top: 36px; "><li><a class="gwt-Anchor" href="javascript:;">Пансионат</a></li><li><a class="gwt-Anchor" href="javascript:;">Студия</a></li><li><a class="gwt-Anchor" href="javascript:;">1 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">2 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">3 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">4 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">5 комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">6+ комнатная</a></li><li><a class="gwt-Anchor" href="javascript:;">По суточно</a></li><li><a class="gwt-Anchor" href="javascript:;">Частный дом</a></li><li><a class="gwt-Anchor" href="javascript:;">Коттедж</a></li><li><a class="gwt-Anchor" href="javascript:;">Другое</a></li></ul></div></div></div>
  </div>

  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Адрес:</label>
    </div>
    <div class="dwformfieldcont eight mobile-three columns" id="dw_" dwname="address"><input type="text" class="dwformfield" placeholder="Обязательно введите адрес"></div>
  </div>
  
  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Цена:</label>
    </div>
    <div class="dwformfieldcont eight mobile-three columns" id="dw_" dwname="cost"><input type="text" class="dwformfield" placeholder="Обязательно введите цену"></div>
  </div>
  
  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Этаж:</label>
    </div>
    <div class="dwformfieldcont eight mobile-three columns" id="dw_" dwname="floor"><input type="text" class="dwformfield"></div>
  </div>
  
  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Площадь:</label>
    </div>
    <div class="dwformfieldcont eight mobile-three columns" id="dw_" dwname="space"><input type="text" class="dwformfield"></div>
  </div>

    <div class="dwformcenter">Дополнительная информация</div>
    <!-- div class="dwformtextarea" id="dw_" dwname="info"><textarea class="gwt-TextArea"></textarea></div -->
  
  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline"></label>
    </div>
    <div class="dwformfieldcont eight mobile-three columns">
      <div class="dwformtextarea" id="dw_" dwname="info"><textarea class="dwformfield"></textarea></div>
    </div>
  </div>
  
  <div class="row dwformrow">
    <div class="labelright three mobile-one columns">
      <label class="right inline">Телефоны:</label>
    </div>
    <div class="dwformfieldcont eight mobile-three columns" id="dw_" dwname="phones"><input type="text" class="dwformfield" placeholder="Вводите телефоны через запятую"></div>
  </div>

    <div class="dwformbuttons"><div class="dwformbutton" id="dw_" dwname="send"><a class="gwt-Anchor button" href="javascript:;">Подать объявление</a></div></div>
</div></div></div>';
    }

    protected function genSubWidget($dwname){
	return '';
    }

}

?>
