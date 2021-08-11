       /* Write a method that merges two already sorted int arrays nums1 and nums2 into one large sorted array nums1.
        Assume that nums1 has enough extra space to fit the solution, and you are provided the size of nums1 without
        extra space as n and the size of nums2 as m. Try to come up with a method that does not use any extra space.

        Example input: [1,2,3,0,0,0] and [2,5,6], n = 3 and m = 3

        Example output: [1,2,2,3,5,6] */
       public static void merge(int nums1[], int nums2[], int n, int m) {
               int merged[] = new int[n+m];
               int ptr1 = 0, ptr2 = 0;

               //As long as there are still elements to consider in both arrays
               while (ptr1 < n && ptr2 < m) {
               if (nums1[ptr1] <= nums2[ptr2]) {
               //If the element in nums1 is less, set the next element of merged to it
               merged[ptr1+ptr2] = nums1[ptr1];
               ptr1++; //Need to increment ptr1 since this element is used up
               }
               else {
               //Otherwise, do the same for nums2 and ptr2
               merged[ptr1+ptr2] = nums2[ptr2];
               ptr2++;
               }
               }

               //If there are leftover elements in nums1, add them all to the end
               while (ptr1 < n) {
               merged[ptr1+ptr2] = nums1[ptr1];
               ptr1++;
               }

               //Same for nums2
               while (ptr2 < m) {
               merged[ptr1+ptr2] = nums2[ptr2];
               ptr2++;
               }

               //Copy merged into nums1
               for (int i = 0; i < n+m; i++) nums1[i] = merged[i];
               }

