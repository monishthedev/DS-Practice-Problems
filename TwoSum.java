/* Write a method that takes in a sorted array of integers and an integer target value, and returns two elements of
the array which add up to this integer target. Assume that there will always be two elements which add to the target.


Example Input: [2, 7, 11, 15], and target = 9

Example Output: [2, 7] */
public static void twoSum(int[] arr, int target){
    int n = arr.length;
    for(int i = 0; i<n;i++){
        for(int j = 1; j<n;j++){
            if(arr[i]+arr[j] = target){
                System.out.print("["+i", "+j"]");
        }
        }
        }
        }