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

package org.dvijok.controls.table;

public class Summators {

	public static Summator getDoubleSummator(){
	
		return new Summator<String>(){
			@Override
			public String sum(String o1, String o2) {
				return Double.toString(Double.parseDouble(o1)+Double.parseDouble(o2));
			}

			@Override
			public String getZero() {
				return "0";
			}
		};
		
	}

	public static Summator getLongSummator(){
	
		return new Summator<String>(){
			@Override
			public String sum(String o1, String o2) {
				return Long.toString(Long.parseLong(o1)+Long.parseLong(o2));
			}

			@Override
			public String getZero() {
				return "0";
			}
		};
		
	}

	public static Summator getConstStringSummator(final String str){
	
		return new Summator<String>(){
			@Override
			public String sum(String o1, String o2) {
				return str;
			}

			@Override
			public String getZero() {
				return str;
			}
		};
		
	}

	public static Summator getCountSummator(){
	
		return new Summator<String>(){
			@Override
			public String sum(String o1, String o2) {
				return Long.toString(Long.parseLong(o1)+1);
			}

			@Override
			public String getZero() {
				return "0";
			}
		};
		
	}
	
}
