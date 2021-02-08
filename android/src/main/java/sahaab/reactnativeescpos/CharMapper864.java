package sahaab.reactnativeescpos;

import java.util.ArrayList;

public class CharMapper864 {
	
	private int getCellNO(String s, int ix){
		char c = s.charAt(ix);
		if (c>=48 && c<=57) return getCellNONum(c);
		if (c>=0x0660 && c<=0x0669) return getCellNOArabNO1(c);
		if (c>=0x06F0 && c<=0x06F9) return getCellNOArabNO2(c);
		if (c>=0 && c<=127) {
			return c;
		}
					
		boolean preConn = 
				(ix!=0) && (!BidiStringBreaker.isSpecialChar(s.charAt(ix-1))) && (connectToNext(s.charAt(ix-1)));
		boolean postConn = 
				(ix!=s.length()-1) && (!BidiStringBreaker.isSpecialChar(s.charAt(ix+1))) &&  (connectToNext(c));
		if (!preConn && !postConn) return isolatedForm(c);
		if (!preConn && postConn) return initialForm(c);
		if (preConn && postConn) return medialForm(c);
		//(preConn && !postConn) 
		return finalForm(c);
	}

	private int getCellNOArabNO2(char c) {
		return c - 1728;
		//return c - 1728 + 187;//return Farsi NO
	}

	private int getCellNOArabNO1(char c) {
		return c - 1584;
		//return c - 1584 + 187;//return Farsi NO
	}

	private int getCellNONum(char c) {
		return c;
		//return c + 187;//return Farsi NO
	}

	protected int isolatedForm(char c) {
		if (c=='ا') return 0xc7;
		if (c=='آ') return 0xc2;
		if (c=='ب') return 0xa9;
		if (c=='ت') return 0xaa;
		if (c=='ث') return 0xab;
		if (c=='ج') return 0xad;
		if (c=='ح') return 0xae;
		if (c=='خ') return 0xaf;
		if (c=='د') return 0xcf;
		if (c=='ذ') return 0xd0;
		if (c=='ر') return 0xd1;
		if (c=='ز') return 0xd2;
		if (c=='س') return 0xbc;
		if (c=='ش') return 0xbd;
		if (c=='ص') return 0xbe;
		if (c=='ض') return 0xeb;
		if (c=='ط') return 0xd7;
		if (c=='ظ') return 0xd8;
		if (c=='ع') return 0xdf;
		if (c=='غ') return 0xee;
		if (c=='ف') return 0xba;
		if (c=='ق') return 0xf8;
		if (c=='ك') return 0xfc;
		if (c=='ل') return 0xfb;
		if (c=='م') return 0xef;
		if (c=='ن') return 0xf2;
		if (c=='و') return 0xe8;
		if (c=='ه') return 0xf3;
		if (c=='ی') return 0xe9;
		if (c=='ي') return 0xfd;
		return otherChars(c);
	}

	protected int initialForm(char c) {
		if (c=='ا') return 0xc7;
		if (c=='آ') return 0xc2;
		if (c=='ب') return 0xc8;
		if (c=='ت') return 0xca;
		if (c=='ث') return 0xcb;
		if (c=='ج') return 0xcc;
		if (c=='ح') return 0xcd;
		if (c=='خ') return 0xce;
		if (c=='د') return 0xcf;
		if (c=='ذ') return 0xd0;
		if (c=='ر') return 0xd1;
		if (c=='ز') return 0xd2;
		if (c=='س') return 0xd3;
		if (c=='ش') return 0xd4;
		if (c=='ص') return 0xd5;
		if (c=='ض') return 0xd6;
		if (c=='ط') return 0xd7;
		if (c=='ظ') return 0xd8;
		if (c=='ع') return 0xd9;
		if (c=='غ') return 0xda;
		if (c=='ف') return 0xe1;
		if (c=='ق') return 0xe2;
		if (c=='ك') return 0xe3;
		if (c=='ل') return 0xe4;
		if (c=='م') return 0xe5;
		if (c=='ن') return 0xf2;
		if (c=='و') return 0xe8;
		if (c=='ه') return 0xe7;
		if (c=='ی') return 0xea;
		if (c=='ي') return 0xea;
		return otherChars(c);		
	}
	
	protected int otherChars(char c) {
		//TODO more characters such as Arabic question mark (?) may be added here.		
		if (c=='،') return 0xf8;
		return 0x5f;
	}

	protected int medialForm(char c) {
		if (c=='ا') return 0xa8;
		if (c=='أ') return 0xa5;
		if (c=='آ') return 0xa5;
		if (c=='ب') return 0xc8;
		if (c=='ت') return 0xca;
		if (c=='ث') return 0xcb;
		if (c=='ج') return 0xcc;
		if (c=='ح') return 0xcd;
		if (c=='خ') return 0xce;
		if (c=='د') return 0xcf;
		if (c=='ذ') return 0xd0;
		if (c=='ر') return 0xd1;
		if (c=='ز') return 0xd2;
		if (c=='س') return 0xd3;
		if (c=='ش') return 0xd4;
		if (c=='ص') return 0xd5;
		if (c=='ض') return 0xd6;
		if (c=='ط') return 0xd7;
		if (c=='ظ') return 0xd8;
		if (c=='ع') return 0xec;
		if (c=='غ') return 0xf7;
		if (c=='ف') return 0xe1;
		if (c=='ق') return 0xe2;
		if (c=='ك') return 0xe3;
		if (c=='ل') return 0xe4;
		if (c=='م') return 0xe5;
		if (c=='ن') return 0xf2;
		if (c=='و') return 0xe8;
		if (c=='ه') return 0xf4;
		if (c=='ی') return 0xea;
		if (c=='ي') return 0xea;
		return otherChars(c);
	}
	
	protected int finalForm(char c) {
		if (c=='ا') return 0xa8;
		if (c=='أ') return 0xa5;
		if (c=='آ') return 0xa5;
		if (c=='ب') return 0xa9;
		if (c=='ت') return 0xaa;
		if (c=='ث') return 0xab;
		if (c=='ج') return 0xad;
		if (c=='ح') return 0xae;
		if (c=='خ') return 0xaf;
		if (c=='د') return 0xcf;
		if (c=='ذ') return 0xd0;
		if (c=='ر') return 0xd1;
		if (c=='ز') return 0xd2;
		if (c=='س') return 0xbc;
		if (c=='ش') return 0xbd;
		if (c=='ص') return 0xbe;
		if (c=='ض') return 0xeb;
		if (c=='ط') return 0xd7;
		if (c=='ظ') return 0xd8;
		if (c=='ع') return 0xc5;
		if (c=='غ') return 0xed;
		if (c=='ف') return 0xba;
		if (c=='ق') return 0xf8;
		if (c=='ك') return 0xfc;
		if (c=='ل') return 0xfb;
		if (c=='م') return 0xef;
		if (c=='ن') return 0xf2;
		if (c=='و') return 0xe8;
		if (c=='ه') return 0xf3;
		if (c=='ی') return 0xf5;
		if (c=='ي') return 0xf6;
		if (c=='ة') return 0xc9;
		return otherChars(c);
	}

	private boolean connectToNext(char preChar) {		
		return !(preChar=='آ' || preChar=='ا' || preChar=='د' || preChar=='ذ' || 
			   preChar=='ر' || preChar=='ز' || preChar=='ژ' || 
			   preChar=='و');
	}

	/**
	 * This method assumes that the prevailing direction is RTL.
	 * @param string to be encoded in the Woosim code
	 * tables (i.e. 42:WoosimCmd.CT_ARABIC_FARSI or 43:CT_ARABIC_FORMS_B or ...).
	 * @return Integer codes for the string.
	 */
	public ArrayList<Integer> getCodes(String string) {
		BidiStringBreaker bidiBreaker = new BidiStringBreaker();
        ArrayList<String> brokenDown = bidiBreaker.breakDown(string);
        ArrayList<Integer> res = new ArrayList<Integer>();
        for (int i = brokenDown.size()-1; i>=0; i--) {
        	String s = brokenDown.get(i);
        	if (bidiBreaker.getKind(s, 0)==BidiStringBreaker.CFarsi){
        		res.addAll(getSubCodesFarsi(s));				
        	}else{
        		res.addAll(getSubCodesEng_Num(s));
        	}
		}        
        return res;            
	}

	private ArrayList<Integer> getSubCodesFarsi(String s) {
		ArrayList<Integer> wCodes = new ArrayList<Integer>();		
    	for (int i = s.length()-1;i>=0;i--){
    		wCodes.add(getCellNO(s, i));
    	}		
    	return wCodes;	
    }

	private ArrayList<Integer> getSubCodesEng_Num(String s) {
		ArrayList<Integer> wCodes = new ArrayList<Integer>();		
    	for (int i = 0;i<s.length();i++){
    		wCodes.add(getCellNO(s, i));
    	}		
    	return wCodes;	
    }
	
}