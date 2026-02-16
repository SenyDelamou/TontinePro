package groupe1;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataExporter {

    public static void exportToCSV(JTable table, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter vers CSV");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Default filename
        fileChooser.setSelectedFile(new File("export_data.csv"));
        
        int userSelection = fileChooser.showSaveDialog(parent);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure .csv extension
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getParent(), fileToSave.getName() + ".csv");
            }
            
            try (FileWriter fw = new FileWriter(fileToSave)) {
                TableModel model = table.getModel();
                
                // Headers
                for (int i = 0; i < model.getColumnCount(); i++) {
                    fw.write(model.getColumnName(i) + (i == model.getColumnCount() - 1 ? "" : ","));
                }
                fw.write("\n");
                
                // Rows
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object val = model.getValueAt(i, j);
                        String str = (val == null) ? "" : val.toString();
                        // Escape commas
                        if (str.contains(",")) {
                            str = "\"" + str + "\"";
                        }
                        fw.write(str + (j == model.getColumnCount() - 1 ? "" : ","));
                    }
                    fw.write("\n");
                }
                
                Toast.show(parent, "Exportation réussie !", Toast.Type.SUCCESS);
                
            } catch (IOException e) {
                Toast.show(parent, "Erreur lors de l'exportation.", Toast.Type.ERROR);
                e.printStackTrace();
            }
        }
    }
}
