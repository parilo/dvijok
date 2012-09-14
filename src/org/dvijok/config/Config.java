//    dvijok - cms written in gwt
//    Copyright (C) 2010  Pechenko Anton Vladimirovich aka Parilo
//    mailto: forpost78 at gmail dot com
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>
//

package org.dvijok.config;

import java.util.Date;

public class Config {

	public String dbUrl;
	public Date sessExpTime;
	
	//vk auth see http://vk.com/developers.php?oid=-1&p=%D0%90%D0%B2%D1%82%D0%BE%D1%80%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F_%D1%81%D0%B0%D0%B9%D1%82%D0%BE%D0%B2
	public String appid;
	public String settings;
	
	public Config(){

//		this.dbUrl = "http://127.0.0.1:8888/dvrpc/rpc.php";
		this.dbUrl = "dvrpc/rpc.php";
		this.sessExpTime = new Date(365*24*60*60*1000); //cookie exp time
		
		this.appid = "2977906";
		this.settings = "notify";
	}
	
}
