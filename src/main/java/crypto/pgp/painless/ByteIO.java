package crypto.pgp.painless;

public class ByteIO {
    //so basically rfc-4880 states that theres a new thing lol
    //this is kinda incomplete but it does its job soooo
    public static String byteToRadix(byte[] arr){
        String f = "";

        byte[] buffer = new byte[3];
        int index = -1;
        for(int i = 0; i < arr.length; i++){
            buffer[i % 3] = arr[i];
            if(i % 3 == 2) {
                char[] norm = {0, 0, 0, 0};
                for(int j = 0; j < 24; j++){
                    if(j % 6 == 0) index++;
                    if(isSet(buffer, j)) norm[index] = (char) (norm[index] + pow(2, 5 - (j % 6)));
                }

                f += String.valueOf(norm);
                buffer = new byte[3];
            }
        }

        return f;
    }

    //RFC - 4880 STATES THAT:
    //that keys shall be displayed in radix 64, or octal based numerical stuff,
    //and it needs to use a new formatting,
    //therefore you need to convert integers to chars ;-;
    private static char radixToChar(int x){
        if(x <= 25) {
            return (char) (x + 65);
        } else if (x <= 51) {
            return (char) (x + 97 - 26);
        } else if (x <= 61) {
            return (char) (x + 48 - 52);
        } else if (x <= 62) {
            return (char) 43;
        } else {
            return (char) 92;
        }
    }

    //algorithm to recursively fetch power
    private static int pow(int val, int pow){
        if(pow != 0){
            return (val * pow(val, pow - 1));
        } else {
            return 1;
        }
    }


    //stolen from stack overflow:
    //link: https://stackoverflow.com/questions/18931283/checking-individual-bits-in-a-byte-array-in-java?msclkid=9fb106adb30611eca6e98ad92f28194f
    public static boolean isSet(byte[] arr, int bit) {
        int index = bit / 8;  // Get the index of the array for the byte with this bit
        int bitPosition = bit % 8;  // Position of this bit in a byte

        return (arr[index] >> bitPosition & 1) == 1;
    }

    //unit test for radix
    public static void main(String[] args) {
        for(int i = 0; i < 64; i++){
            System.out.println(radixToChar(i));
        }
    }
}
