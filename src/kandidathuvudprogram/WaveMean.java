/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kandidathuvudprogram;




/**
 *
 * @author davsven
 */
public class WaveMean {
    
    
    public static void main(String[] arg){
        int xRawSize = 85*8;
        int yRawSize = 35*8;
        String[] dataString = new String[xRawSize*yRawSize]; 
        Import imp = new Import(); 
        
        dataString = imp.importWhole("wavedata/raw_20140105_00.tsv"); 
        double[][] dataDouble = new double[yRawSize][xRawSize];
        for(int n = 0; n < yRawSize; n++){
            for(int m = 0; m < xRawSize; m++) {
                dataDouble[n][m] = Double.parseDouble(dataString[n*xRawSize + m]);
            }
        }
        
        double[][] dataMean = new double[35][85];
        dataMean = calculateWaveMean(1,dataDouble);
        
        
    }
    
    public static double[][] calculateWaveMean(int res, double[][] waveData){
        
        res = 1;
        int[] range = {-70,15, 35, 70 }; // Förbestämt område, ändra ej!
        
        int xLength = 85/res;
        int yLength = 35/res;
        double[][] tempData = new double[2][xLength];
        double[][] meanWaveData = new double[yLength][xLength];
 
        
        waveData = expandDoubleArray(res, waveData); //
        

        
        for(int a = 0;a < waveData.length/(8*res); a = a + 8*res){ // Alla for-loopar är i termer av index i expandDoubleArray
            for(int n = a; n < a +8*res; n++){
               for(int b = 0; b < waveData[0].length/(8*res); b = b + 8*res ){
                   for(int m = b; m < b + 8*res; m++){
                   
                       if (waveData[n][m] != -1){
                           tempData[0][b/(8*res)] = tempData[0][b/(8*res)] + waveData[n][m]; //Summa våghöjd 
                           tempData[1][b/(8*res)] = tempData[1][b/(8*res)] + 1 ; // Antal datapunkter
                       }
                               
                       
                       
                   }
                   
                   
               }
                
            }
            for(int i = 0;i < xLength; i++ ){
                
                if(tempData[1][i] > 0){
                    meanWaveData[a/(8*res)][i] = tempData[0][a]/tempData[1][a];
                }else{
                        meanWaveData[a/(8*res)][i] = -1; // Om det inte finns någon data inom området
                        
                        }
            }
        }
        
        
        return meanWaveData; 
    }

    public static double[][] expandDoubleArray(int res,  double[][] dArray){
        
        double[][] expandedArray = new double[dArray.length + 8*res -1][dArray[0].length + 8*res -1];
        
        for(int a = 0; a < expandedArray.length; a++ ){ // Fyller array med -1
            for(int b = 0; b< expandedArray[a].length; b++){
                expandedArray[a][b] = -1;
            }
        }
        
        for(int n = 4*res - 1; n < expandedArray.length -4*res ; n++){ // Fyller mitten av array med input
            for(int m = 4*res - 1; m < expandedArray.length - 4*res; m++){
            
                expandedArray[n][m] = dArray[n -4*res +1][m -4*res +1];
            }
        
        }
        
        return expandedArray;
        
    }
}

