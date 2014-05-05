/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package kandidathuvudprogram;

import static kandidathuvudprogram.GetWaveDataHgsChalmers.downloadWaveData;
import static kandidathuvudprogram.GetWaveDataHgsChalmers.generateDateString;
import java.io.File;






/**
*
* @author davsven
*/
public class WaveMean {

        static int xSize = 85;
        static int ySize = 35;

    public static void main(String[] arg){

        //removeMissing("2010-01-01","2014-04-01");
        buildMap();
    }


 
    
    /**
* Hämtar data, beräknar medelvärdet och kollar var data saknas. Hämtar all tillgänglig data mellan startDate och endDate.
* @param startDate datum på formen "YYYY-MM-DD", Finns från 201007
* @param endDate datum på formen "YYYY-MM-DD"
*/
    
    public static void fetchMean(String startDate, String endDate){
        
        GetWaveDataHgsChalmers.downloadWaveData(startDate,endDate, true);
        String[] dateArray = GetWaveDataHgsChalmers.generateDateString(startDate, endDate);
        String[] dateHrArray = new String[dateArray.length*4];
        String[] hrs = new String[]{"00","06","12","18"};
        for (int n=0; n<dateArray.length; n++){ // skapar en ny String array med datum och klockslag
            for (int m=0; m<4; m++){
                dateHrArray[n*4+m] = dateArray[n]+ "_" + hrs[m];
            }
        }
        
        for(int i = 0; i < dateHrArray.length; i++){
            File f = new File("wavedata/raw/20" + dateHrArray[i] + ".tsv");
            File g = new File("wavedata/20" + dateHrArray[i] + ".tsv");
            if(f.exists() && !g.exists()){
              
                double[][] dataDouble = new double[ySize*8][xSize*8];
                dataDouble = importAndFormat("wavedata/raw/20" + dateHrArray[i] + ".tsv");
                        
                double[][] dataMean = new double[35][85];
                dataMean = calculateWaveMean(1,dataDouble);

                try{
                    Utskrift.write2Matrix("wavedata/20" + dateHrArray[i] + ".tsv",dataMean,"," );
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

            addMissing("wavedata/20" + dateHrArray[i] + ".tsv");
            }
            
        }
        
    }
    
    /**
* Beräknar medelvärdet av rutor med storleken 1 kvadratgrad i en 2D double-array.
* @param res Upplösning i grader. Används ej , alltid 1.
* @param waveData 2D double array vars medelvärde ska beräknas. Ska ha storleken 8*(35x80).
* @return 2D double-array med storleken 35x80 där varje punkt är medelvärdet av "rutan".
*/

    public static double[][] calculateWaveMean(int res, double[][] waveData){

        res = 1; // Kanske orkar fixa andra upplösningar senare
        int[] range = {-70,15, 35, 70 }; // Förbestämt område, ändra ej!

        int xLength = 85/res;
        int yLength = 35/res;
        int[] count = {0,0,0};
        double[][] tempData = new double[2][xLength]; // Temporär fil för att beräkna medelvärde
        double[][] meanWaveData = new double[yLength][xLength];

        for(int a = 0;a < (waveData.length); a = a + 8*res){ // Alla for-loopar är i termer av index i expandDoubleArray
            for(int n = a; n < a +8*res; n++){
               for(int b = 0; b < waveData[0].length; b = b + 8*res ){
                   for(int m = b; m < b + 8*res; m++){

                       if (waveData[n][m] != -1){ //Om det finns mätdata i punkten
                           tempData[0][b/(8*res)] = tempData[0][b/(8*res)] + waveData[n][m]; //Summa våghöjd i rutan
                           tempData[1][b/(8*res)] = tempData[1][b/(8*res)] + 1 ; // Antal datapunkter i rutan

                        }
                    }
                }
            }
            for(int i = 0;i < tempData[1].length; i++ ){

                if(tempData[1][i] > 0){
                    meanWaveData[a/(8*res)][i] = tempData[0][i]/tempData[1][i];

                }else{
                        meanWaveData[a/(8*res)][i] = -1; // Om det inte finns någon data inom området
                        }
                }
            for(int j = 0; j < tempData[1].length; j++ ){ //Nollställ temp
                tempData[0][j] = 0;
                tempData[1][j] = 0;
            }


        }


        return meanWaveData;
    }

    /**
* Expanderar en 2D double-array med marginal för att förhindra fel vid uträkning av medelvärde. De nya elmenten har värdet -1.
* @param res Upplösning i grader. Används ej , alltid 1.
* @param dArray 2D double-array med data.
* @return dArray med marginaler
*/

    public static double[][] expandDoubleArray(int res, double[][] dArray){
        res = 1;

        double[][] expandedArray = new double[dArray.length + 8*res -1][dArray[0].length + 8*res -1];

        for(int a = 0; a < expandedArray.length; a++ ){ // Fyller array med -1
            for(int b = 0; b< expandedArray[a].length; b++){
                expandedArray[a][b] = -1;
            }
        }

        for(int n = 0; n < dArray.length ; n++){ // Fyller mitten av array med input
            for(int m = 0; m < dArray[n].length ; m++){

                expandedArray[n+res*4 -1][m+res*4 -1] = dArray[n][m];

            }

        }

        return expandedArray;

    }
    /**
* Importerar en 1-vektor med data och skriver den som en 2D double-array.
* @param file Filens sökväg och namn
* @return En 2D double-array med storleken (rawYSize*rawXSize)
*/

    //public static double[][] importAndFormat(String file, int rawXSize, int rawYSize){
    public static double[][] importAndFormat(String file){


        int rawXSize = xSize*8;
        int rawYSize = ySize*8;


        String[] dataString = new String[rawXSize*rawYSize];
        Import imp = new Import();

        dataString = imp.importWhole(file); //Läser fil till doubleArray
        double[][] dataDouble = new double[rawYSize][rawXSize];
        for(int n = 0; n < rawYSize; n++){
            for(int m = 0; m < rawXSize; m++) {
                dataDouble[n][m] = Double.parseDouble(dataString[n*(rawXSize+1) + m]);
            }
        }

        return dataDouble;
    }
    
    /**
* Kollar var data saknas och skriver inofrmationen i "missingWave.csv"
* @param fileName Sökvägen och filnamnent av filen som ska räknas. Filen ska vara output från calculateWaveMean
*/
    
    static void addMissing(String fileName){
       
        
        Import imp = new Import();
        String[] stringData = new String[ySize];
        String[] stringTemp = new String[xSize];
        
        String[] stringMissing = new String[ySize];
        String[] stringMissingTemp = new String[xSize];
        
        stringData = imp.importWhole(fileName);
        stringMissing = imp.importWhole("missingWave.csv");
        double[][] doubleMissing = new double[ySize][xSize];
        
        for(int n = 0; n < ySize; n++){
            stringTemp = stringData[n].split(",");
            stringMissingTemp = stringMissing[n].split(",");
            
            for(int m = 0; m < xSize ; m++){
                doubleMissing[n][m] = Double.parseDouble(stringMissingTemp[m]);
                if(stringTemp[m].equals("-1.0")){
                    doubleMissing[n][m]= doubleMissing[n][m] + 1;
                    
                }
            }
        }
        try{ //Sparar doubleArray till CSV
            Utskrift.write2Matrix("missingWave.csv",doubleMissing,"," );
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
    
    static void removeMissing(String startDate, String endDate){
        
        String[] dateArray = GetWaveDataHgsChalmers.generateDateString(startDate, endDate);
        String[] dateHrArray = new String[dateArray.length*4];
        String[] hrs = new String[]{"00","06","12","18"};
        for (int n=0; n<dateArray.length; n++){ // skapar en ny String array med datum och klockslag
            for (int m=0; m<4; m++){
                dateHrArray[n*4+m] = dateArray[n]+ "_" + hrs[m];
            }
        }
        
        
        for(int i = 0; i < dateHrArray.length; i++){
            File f = new File("wavedata/20" + dateHrArray[i] + ".tsv");
            File g = new File("wavedata/removedmissing/20" + dateHrArray[i] + ".tsv");
            int numOfMissing = 0;
            if(f.exists() && !g.exists()){
              
                
                Import imp = new Import();
                String[] stringData = new String[ySize];
                String[] stringTemp = new String[xSize];
        
                String[] stringMissing = new String[ySize];
                String[] stringMissingTemp = new String[xSize];
        
                stringData = imp.importWhole("wavedata/20" + dateHrArray[i] + ".tsv");
                stringMissing = imp.importWhole("missingWave.csv");
                double[] doubleWOMissing = new double[2003];
        
                for(int n = 0; n < ySize; n++){
                    stringTemp = stringData[n].split(",");
                    stringMissingTemp = stringMissing[n].split(",");
            
                    for(int m = 0; m < xSize ; m++){
                       
                        if(stringMissingTemp[m].equals("0.0")){
                            doubleWOMissing[numOfMissing] = Double.parseDouble(stringTemp[m]);
                            numOfMissing++;
                            
                    
                        }
                            
                         
                    }
                }


                try{
                    Utskrift.write("wavedata/removedmissing/20" + dateHrArray[i] + ".tsv",doubleWOMissing);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }

            }
            
        }
        
    }
    static void buildMap(){
        
        int numOfMissing = 0;
        Import imp = new Import();
        
        String[] stringMissing = new String[ySize];
        String[] stringMissingTemp = new String[xSize];
        stringMissing = imp.importWhole("missingWave.csv");
        double[][] doubleWOMissing = new double[2003][2];
        
        for(int n = 0; n < ySize; n++){
            stringMissingTemp = stringMissing[n].split(",");
            for(int m = 0; m < xSize ; m++){
                       
                if(stringMissingTemp[m].equals("0.0")){
                    doubleWOMissing[numOfMissing][0] = (-70 + m);
                    doubleWOMissing[numOfMissing][1] = ( 70 - n);
                    numOfMissing++;
                }
            }
        }

        try{
            Utskrift.write2Matrix("wavedata/removedmissing/map.tsv",doubleWOMissing, "\t");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
            
}

