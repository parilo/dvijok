<?php

require_once 'config.def.php';
require_once 'dvipc.php';

	echo date_timestamp_get(date_create())."\n";
	$event['type'] = 'idle';
	
	$ipc = new DVIPCSys();
	$ipc->invokeEvent($event);

?>