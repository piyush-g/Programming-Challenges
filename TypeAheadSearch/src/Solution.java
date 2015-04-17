import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args)
    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("Exception: "+e);
        }
        if(scanner==null)
            return;
        int num = scanner.nextInt();
        scanner.nextLine();
        processNextNLines(num, scanner);
    }
    public static void processNextNLines(int n, Scanner scanner)
    {
        final TypeAheadService service = TypeAheadService.getService();
        for(int i=0;i<n;i++)
        {
            service.executeCommand(scanner.nextLine());
        }
    }
}
