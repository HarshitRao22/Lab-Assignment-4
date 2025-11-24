import java.io.*;
import java.util.*;

public class LabAssignment4 {

    // -------------------- STUDENT CLASS --------------------
    static class Student {
        private int roll;
        private String name;
        private String email;
        private String course;
        private double marks;

        public Student(int roll, String name, String email, String course, double marks) {
            this.roll = roll;
            this.name = name;
            this.email = email;
            this.course = course;
            this.marks = marks;
        }

        public int getRoll() { return roll; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getCourse() { return course; }
        public double getMarks() { return marks; }

        @Override
        public String toString() {
            return "Roll No: " + roll +
                   "\nName: " + name +
                   "\nEmail: " + email +
                   "\nCourse: " + course +
                   "\nMarks: " + marks + "\n";
        }
    }

    // -------------------- FILE UTIL CLASS --------------------
    static class FileUtil {

        private static final String FILE_NAME = "students.txt";

        // Load students from file
        public static ArrayList<Student> loadStudents() {
            ArrayList<Student> list = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");

                    int roll = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    String course = parts[3];
                    double marks = Double.parseDouble(parts[4]);

                    list.add(new Student(roll, name, email, course, marks));
                }

                System.out.println("Loaded students from file:");
                for (Student s : list) {
                    System.out.println(s);
                }

            } catch (Exception e) {
                System.out.println("File empty or not found. Starting with empty list.");
            }

            return list;
        }

        // Save students to file
        public static void saveStudents(ArrayList<Student> students) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
                for (Student s : students) {
                    bw.write(s.getRoll() + "," + s.getName() + "," +
                             s.getEmail() + "," + s.getCourse() + "," +
                             s.getMarks());
                    bw.newLine();
                }
            } catch (Exception e) {
                System.out.println("Error saving students.");
            }
        }

        // Demonstrate RandomAccessFile
        public static void readRandom() {
            try (RandomAccessFile raf = new RandomAccessFile(FILE_NAME, "r")) {
                System.out.println("\nRandomAccessFile Demo:");
                System.out.println("First 20 characters from file:");
                byte[] bytes = new byte[20];
                raf.read(bytes);
                System.out.println(new String(bytes));
            } catch (Exception e) {
                System.out.println("RandomAccessFile error.");
            }
        }
    }

    // -------------------- STUDENT MANAGER CLASS --------------------
    static class StudentManager {

        private ArrayList<Student> students;

        public StudentManager() {
            students = FileUtil.loadStudents();
        }

        Scanner sc = new Scanner(System.in);

        public void addStudent() {
            System.out.print("Enter Roll No: ");
            int roll = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();

            System.out.print("Enter Marks: ");
            double marks = sc.nextDouble();
            sc.nextLine();

            students.add(new Student(roll, name, email, course, marks));
        }

        public void viewAll() {
            Iterator<Student> it = students.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }
        }

        public void searchByName() {
            System.out.print("Enter name to search: ");
            String name = sc.nextLine();

            boolean found = false;

            for (Student s : students) {
                if (s.getName().equalsIgnoreCase(name)) {
                    System.out.println("\nFound:\n" + s);
                    found = true;
                }
            }

            if (!found)
                System.out.println("Student not found.");
        }

        public void deleteByName() {
            System.out.print("Enter name to delete: ");
            String name = sc.nextLine();

            Iterator<Student> it = students.iterator();
            boolean deleted = false;

            while (it.hasNext()) {
                Student s = it.next();
                if (s.getName().equalsIgnoreCase(name)) {
                    it.remove();
                    deleted = true;
                    System.out.println("Record deleted.");
                }
            }

            if (!deleted)
                System.out.println("Student not found.");
        }

        public void sortByMarks() {
            students.sort(Comparator.comparingDouble(Student::getMarks).reversed());
            System.out.println("\nSorted Student List by Marks:");
            viewAll();
        }

        public ArrayList<Student> getList() {
            return students;
        }
    }

    // -------------------- MAIN PROGRAM --------------------
    public static void main(String[] args) {

        StudentManager manager = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: manager.addStudent(); break;
                case 2: manager.viewAll(); break;
                case 3: manager.searchByName(); break;
                case 4: manager.deleteByName(); break;
                case 5: manager.sortByMarks(); break;
                case 6:
                    FileUtil.saveStudents(manager.getList());
                    FileUtil.readRandom();
                    System.out.println("Records saved. Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
