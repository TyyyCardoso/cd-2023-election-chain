/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electionspoo.gui;

import electionspoo.beans.candidate.CandidateBean;
import electionspoo.beans.election.ElectionBean;
import javax.swing.JDialog;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Tiago
 */
public class GUIResultsStatistics extends JDialog {
    public GUIResultsStatistics(ElectionBean election){
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Grafico de Barra");
        setSize(960, 700);
        setLocationRelativeTo(null);
        criarGrafico(election);
        setVisible(true);
    }
    
    public void criarGrafico(ElectionBean election){
        DefaultCategoryDataset barra = new DefaultCategoryDataset();
        for (CandidateBean candidate : election.getCandidateList()){
            barra.setValue(candidate.getVotes(), candidate.getName(), "");
        }
        JFreeChart grafico = ChartFactory.createBarChart("A", "B", "C", barra);
        ChartPanel painel = new ChartPanel(grafico);
        add(painel);
    }
    
    public static void main(String[] args) {
        //new TestChartsYa();
    }
}
