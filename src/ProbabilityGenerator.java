import java.util.*;
import java.math.BigInteger;


public class ProbabilityGenerator
{
    public static final int NUMBER_OF_DICE = 4;
    public static final int RANGE = 6;
    
    
    public static void main(String[] args) {
        ProbabilityGenerator p = new ProbabilityGenerator();
        // order of inputs: number of die, range of each dice, and the target sum
        double total = 1;
        for(int i = 0; i < NUMBER_OF_DICE; i++) {
            total = total * RANGE;
        }
        
//        double result = 0;
//        for(int i = 4; i < 25; i++) {
//            result = result + p.numberOfArrangements(NUMBER_OF_DICE, RANGE, i);
//        }
//        // result = result + p.numberOfArrangements(NUMBER_OF_DICE, RANGE, 350);
//        System.out.println("VERIFICATION: total probability adds up to: " + result + " / " + total);  
        int [][] A = new int[32][32];
        int [][] B = new int[32][32];
     
        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 32; j++) {
                A[i][j] = (int)(Math.random() * 1000);
                
            }
        }
        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 32; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        p.matrix(A, B);
        System.out.println();
        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 32; j++) {
                System.out.print(B[i][j] + " ");
            }
            System.out.println();
        }
        
        System.out.println();
        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 32; j++) {
                if(A[i][j] != B[j][i]) {
                    System.out.println("i: " + i + "j: gooby" + j);
                }
            }
            
        }
    }

    
    double[] factorial;
    public double numberOfArrangements(int numOfDie, int range, int target) {
        factorial = new double[numOfDie + 1];
        factorial();
        ArrayList<Double> track = new ArrayList<Double>();
        HashMap<Integer, Integer> repeats = new HashMap<>();
        int[] numbers = new int[numOfDie * range];
        for(int i = 0; i < range; i++) {
            for(int c = 0; c < numOfDie; c++) {
                numbers[i * numOfDie + c] = i + 1;
                // System.out.print(numbers[i * numOfDie + c] + " ");
            }
        }
        System.out.println();
        System.out.println("number of die: " + numOfDie + ", range of dice: " + range + ", sum target: " + target);
        // unique arrangements (set)
        System.out.println("number of unique arrangements: " + arrangementHelper(numbers, 0, numOfDie, target, 1, track, repeats));
        double result = 0;
        for(int i = 0; i < track.size(); i++) {
            // System.out.println(track.get(i));
            result = result + track.get(i);
        }
        double total = 1;
        for(int i = 0; i < numOfDie; i++) {
            total = total * range;
        }
        System.out.println("probability: " + result + " / " + total);
        return result;
    }
    // numbers array represent all possible dice numbers
    private int arrangementHelper(int[] numbers, int index, int numOfDie, int target, int depth, ArrayList<Double> track, HashMap<Integer, Integer> repeats) {
        if(depth == numOfDie - 1) {
            int i = index;
            int k = numbers.length - 1;
            int count = 0;
            if(target > numbers[k] * 2 || target < numbers[0] * 2)
                return count;
            while(i < k) {
                if(numbers[i] + numbers[k] < target) {
                    i = calculateI(numbers, i);
                }
                else if(numbers[i] + numbers[k] > target) {
                    k = calculateK(numbers, k);
                }
                else
                {
                    // System.out.println(numbers[i] + " " + numbers[k] + " " + target);
                    // System.out.println(repeats);
                    mapSetUp(repeats, numbers[i], numbers[k], track);
                    i = calculateI(numbers, i);
                    k = calculateK(numbers, k);
                    count++;
                }
            }
            return count;
        }
        int count = 0;
        for(int i = index; i < numbers.length - 2; i++) {
            int leftover = numOfDie - depth;
            if(leftover > numbers.length - i - 1)
                return count;
            else if(target - numbers[i] < numbers[i + 1] * leftover)
                return count;
            else if(target - numbers[i] > numbers[numbers.length - 1] * leftover) {
                // skipping
                i = calculateI(numbers, i); i--;
            }
            else {
                Integer temp = repeats.get(numbers[i]);
                if(temp != null) {
                    repeats.put(numbers[i], temp + 1);
                }else {
                    repeats.put(numbers[i], 1);
                }
                // index should be i + 1, not index + 1
                int ret = arrangementHelper(numbers, i + 1, numOfDie, target - numbers[i], depth + 1, track, repeats);
                if(ret != 0) {
                    if(temp == null) {
                        repeats.remove(numbers[i]);
                    }
                    else
                        repeats.put(numbers[i], temp);
                    count = count + ret;
                }
                // prevent repetition by skipping
                i = calculateI(numbers, i); i--;
            }
        }
        return count;
    }
    
    // calculate i value for repetition
    private int calculateI(int[] numbers, int i) {
        while(i + 1 < numbers.length && numbers[i + 1] == numbers[i])
            i++;
        i++;
        return i;
    }
    
 // calculate k value for repetition
    private int calculateK(int[] numbers, int k) {
        while(k - 1 > 0 && numbers[k - 1] == numbers[k])
            k--;
        k--;
        return k;
    }
    

    
    // set up the map for probability calculations and store value in arraylist
    private void mapSetUp(HashMap<Integer, Integer> repeats, int i, int j, ArrayList<Double> track) {
        Integer temp = repeats.get(i);
        if(temp != null) {
            repeats.put(i, temp + 1);
        }else {
            repeats.put(i, 1);
        }
        
        Integer other = repeats.get(j);
        if(other != null) {
            repeats.put(j, other + 1);
        }else {
            repeats.put(j, 1);
        }
        Set<Integer> set = repeats.keySet();
        double result = factorial[factorial.length - 1];
        // System.out.println(repeats);
        for(int a: set) {
            
            result = result / factorial[repeats.get(a)];
        }
        track.add(result);
        // REMOVE FROM MAP
        if(temp == null) {
            repeats.remove(i);
        }else {
            repeats.put(i, temp);
        }
        if(j != i) {
            if(other == null) {
                repeats.remove(j);
            }else {
                repeats.put(j, other);
            } 
        }
    }
    
    // factorial calculate
    private void factorial() {
        factorial[0] = 1;
        for(int i = 1; i < factorial.length; i++) {
            factorial[i] = factorial[i - 1] * i;
        }
    }
    
    
    /*
     int i = numbers[index];
            // k is the starting point
            int k = 0;
            int[] temp;
            if(index % numOfDie == 5) {
                temp = new int[(numbers.length / numOfDie - i) * 2 + 1];
                temp[0] = i;
                i = i + 1;
                k++;
            }
            else {
                temp = new int[(numbers.length / numOfDie - i + 1) * 2];
            }
            while(k < temp.length) {
                temp[k] = i;
                temp[k + 1] = i;
                k = k + 2;
            }
            i = 0;
            k = temp.length - 1;
            int count = 0;
            if(target > temp[k] * 2 || target < temp[0] * 2)
                return count;
            while(i < k) {
                if(temp[i] + temp[k] < target) {
                    i++;
                }
                else if(temp[i] + temp[k] > target)
                    k--;
                else
                {
                    mapSetUp(repeats, temp[i], temp[k]);
                }
                    
            }
     
     */
   
    public void matrix(int[][] A, int[][] B) {
        int i, j, tmp, row, col, a, b;
        

        for(col = 0; col < 32; col = col + 8){
            for(row = 0; row < 32; row = row + 4){
                for (i = row; i < row + 4; i = i + 1) {
                    //index = -1;
                    for (j = col; j < col + 8; j = j + 1) {
                        //System.out.println("j " + j + " i dsa" + i);
                        if(j < col + 4){
                            //System.out.println(" i " + i + " j " + j);
                            B[i][j] = A[j][i];
                        }else{
                            B[j][i] = A[j][i];
                        }
             
                     
                    }
                    // if(index > -1){
                    //  B[index][index] = temp;
                    // }
                }
                i = row;
                j = col + 4;
                
                if(i == j) {
                    for (i = row; i < row + 4; i = i + 1) {
                        for(j = i; j < col + 8; j++){
                            System.out.println("i : " + i + " j: " + j + " gooby");
                            a = B[j][i];
                            B[j][i] = B[i][j];
                            B[i][j]= a;
                            
                        }
                    }
                }else {
                    for (i = row; i < row + 4; i = i + 1) {
                        for(j = col + 4; j < col + 8; j++){
                            System.out.println("i : " + i + " j: " + j);
                            a = B[j][i];
                            b = B[i][j];
                            B[i][j] = a;
                            B[j][i] = b;
                            
                        }
                    }
                }
                
                
             
            }

        }

    }
}
