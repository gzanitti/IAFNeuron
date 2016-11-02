package com.gzanitti;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static final int TOTAL_TIME = 50;
    private static final double STEP_TIME = 0.125;
    private static final double[] TIME_ARRAY = new double[(int)(TOTAL_TIME/STEP_TIME)];

    private static double[] vm = new double[TIME_ARRAY.length];     //Potencial trace over time
    private static int rm = 1;                                      //Resistance
    private static int cm = 10;                                     //Capacitance
    private static int tau_m = rm*cm;                               //Time constant
    private static double tau_ref = 0.4;                            //Refactory period
    private static int vth = 1;                                     //Spike threshold
    private static double v_spike = 0.5;                            //Spike delta
    private static  double t_rest = 0;                              //Initial refactory time

    private static double I = 1.5;                                  //Input current


    public static void main(String[] args) {
        TIME_ARRAY[0] = 0;
        for (int i = 1; i < TIME_ARRAY.length; i++) {
            TIME_ARRAY[i] = TIME_ARRAY[i-1] + STEP_TIME;
        }

        DataTable data = new DataTable(Double.class, Double.class);
        for (int i = 0; i < TIME_ARRAY.length; i++) {
            if(TIME_ARRAY[i] > t_rest) {
                vm[i] = vm[i-1] + (-vm[i-1] + I*rm) / tau_m * STEP_TIME;
                if(vm[i] >= vth){
                    vm[i] += v_spike;
                    t_rest = TIME_ARRAY[i] + tau_ref;
                }
                data.add(TIME_ARRAY[i], vm[i]);
            }
        }

        LinePlot frame = new LinePlot(data);
        frame.setVisible(true);

    }
}

class LinePlot extends JFrame {
    public LinePlot(DataTable data) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        XYPlot plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderers(data, lines);
    }
}
