import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Student2 extends JFrame
{
    //Input
    private JTextField marksInput;
    private JButton calculateButton;
    //Output
    private JLabel totalMarksResultLabel;
    private JLabel averagePercentageResultLabel;
    private JLabel gradeResultLabel;

    //Constructor which sets up entire window
    public Student2()
    {
        //Setting up the main window
        setTitle("Student Grade Calculator");
        setSize(400, 300); //width and height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); //To center the window

        //Setting up the layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Row 1 Input label
        JLabel inputLabel = new JLabel("Enter marks separated by commas: ");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(inputLabel, gbc);

        //Row 2 Input text field
        marksInput = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(marksInput, gbc);

        //Row 3 Calculate button
        calculateButton = new JButton("Calculate Grade");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; //Spans across 2 columns
        add(calculateButton, gbc);

        //Row 4 Total marks label
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Total Marks: "), gbc);

        totalMarksResultLabel = new JLabel("_");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(totalMarksResultLabel, gbc);

        //Row 5 Avg percentage
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Average Percentage: "), gbc);

        averagePercentageResultLabel = new JLabel("_");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(averagePercentageResultLabel, gbc);

        //Row 6 Grade
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(new JLabel("Final Grade: "), gbc);

        gradeResultLabel = new JLabel("_");
        gradeResultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(gradeResultLabel, gbc);

        calculateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                calculateAndDisplayResults();
            }
        });
    }

    private void calculateAndDisplayResults()
    {
        String inputText = marksInput.getText();
        if(inputText == null || inputText.trim().isEmpty())
        {
            showError("Please enter at least one mark.");
            return;
        }
        String[] marksArray = inputText.split(",");
        int totalMarks = 0;
        int numSubjects = 0;

        try
        {
            for (String markStr : marksArray)
            {
                String trimmedMark = markStr.trim();
                if(trimmedMark.isEmpty()) continue;
                int mark = Integer.parseInt(trimmedMark);//to convert text to number

                //validation
                if(mark < 0 || mark > 100)
                {
                    showError("Invalid mark: "+ mark +". Marks must be between 0 and 100.");
                    resetResults();
                    return;
                }
                totalMarks += mark;
                numSubjects++;
            }
            if(numSubjects == 0)
            {
                showError("No valid marks were entered.");
                return;
            }
            double averagePercentage = (double) totalMarks / numSubjects;
            char grade = calculateGrade(averagePercentage);

            // Update UI
            totalMarksResultLabel.setText(String.valueOf(totalMarks));
            averagePercentageResultLabel.setText(String.format("%.2f%%", averagePercentage));
            gradeResultLabel.setText(String.valueOf(grade));
        }
        catch(NumberFormatException ex)
        {
            showError("Invalid input. Please enter numbers only, separated by commas.");
            resetResults();
        }
    }

    private char calculateGrade(double percentage)
    {
        if(percentage >= 90) return 'A';
        if(percentage >= 80) return 'B';
        if(percentage >= 70) return 'C';
        if(percentage >= 60) return 'D';
        return 'F';
    }

    private void showError(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void resetResults()
    {
        totalMarksResultLabel.setText("_");
        averagePercentageResultLabel.setText("_");
        gradeResultLabel.setText("_");
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new Student2().setVisible(true);
            }
        });
    }
}