package com.example.jwwapp2.tool;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class VerifyBreaker{ 
	static final int ROWN=12;
	static final int COLN=8;
public static int judge(int offset,Bitmap bi)
{
	int startcol=5+(offset)*(COLN+1);
	int endcol=startcol+COLN-1;
	int startrow=5;
	int endrow=16;
	boolean numarr[][]=new boolean [ROWN][COLN];
	int rgb[]=new int[3];
	for(int i=startcol;i<=endcol;i++)
		for(int j=startrow;j<=endrow;j++)
		{
			int pixel=bi.getPixel(i, j); 
			rgb[0] = (pixel & 0xff0000 ) >> 16 ; 
			rgb[1] = (pixel & 0xff00 ) >> 8 ; 
			rgb[2] = (pixel & 0xff ); 
			if(rgb[0]==rgb[1]&&rgb[1]==rgb[2])
				numarr[j-startrow][i-startcol]=false;//void 
			else
				numarr[j-startrow][i-startcol]=true;
		}
	return getnum(numarr);
}
public static int getnum(boolean numarr[][])
{
	int rowcount[]=new int[ROWN];
	int colcount[]=new int[COLN];
	for(int i=0;i<ROWN;i++)
		rowcount[i]=0;
	for(int i=0;i<COLN;i++)
		colcount[i]=0;
	for(int i=0;i<ROWN;i++)
		for(int j=0;j<COLN;j++)
		{
			if(numarr[i][j])
			{
				rowcount[i]++;
				colcount[j]++;
			}
		}
////	for(int i=0;i<ROWN;i++)
//		System.out.println("Row "+i+" count "+rowcount[i]);
//	for(int i=0;i<COLN;i++)
//		System.out.println("Col "+i+" count "+colcount[i]);
	if(colcount[0]==0)
		return 1;
	else if(rowcount[2]==5)
		return 2;
	else if(rowcount[0]==8)
		return 7;
	else if(rowcount[4]==6)
		return 5;
	else if(colcount[7]==7)
		return 3;
	else if(colcount[7]==2)
		return 4;
	else if(colcount[6]==10&&colcount[7]==6)
		return 8;
	else if(rowcount[0]==5)
		return 6;
	else
		return 0;
}

public static String GetImgNum(ArrayList<Byte> al)
{
    byte b[] = new byte[al.size()];
    for(int i=0;i<al.size();i++)
    	b[i] = al.get(i);
	String res = "";
	Bitmap bm = BitmapFactory.decodeByteArray(b, 0, al.size());
	System.out.println("in getimgnum");
		for(int i=0;i<5;i++)
			res+=String.valueOf(judge(i,bm));
		System.out.println(res);
	return res;
}
public static void main(String args[]) { 

}
}