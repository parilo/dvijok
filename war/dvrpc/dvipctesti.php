<?php

require_once 'dvipc.php';

$ipcsys = new DVIPCSys();

$event['obj'] = 'aaa';

$ipcsys->invokeEvent($event);

//$this->putEnvToBus("aaaa", "aaaaval");
//print "env: ".$this->getEnvFromBus("aaaa")."\n";
//$this->removeEnvFromBus("aaaa");

//  		$this->putToQueue("aa", "aaa1");
//  		$this->putToQueue("aa", "aaa2");
//  		$this->putToQueue("aa", "aaa3");
//  		$this->putToQueue("aa", "aaa4");
//  		$this->putToQueue("aa", "aaa5");
 		
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";
//  		print "queue: ".$this->getFromQueue("aa")."\n";

?>
