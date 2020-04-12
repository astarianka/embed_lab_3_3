package com.example.embedlab3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText a_value;
    private EditText b_value;
    private EditText c_value;
    private EditText d_value;
    private EditText y_value;
    private EditText mut_value;
    private TextView out_res;
    private Button button_calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }

    public void addListenerOnButton() {

        a_value = (EditText) findViewById(R.id.a_value);
        b_value = (EditText) findViewById(R.id.b_value);
        c_value = (EditText) findViewById(R.id.c_value);
        d_value = (EditText) findViewById(R.id.d_value);
        y_value = (EditText) findViewById(R.id.y_value);
        mut_value = (EditText) findViewById(R.id.mut_value);
        out_res = (TextView) findViewById(R.id.out_res);
        button_calc = (Button) findViewById(R.id.button);

        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int [] result;
                int a,b,c,d,y;
                double mut;

                String a_str = a_value.getText().toString();
                String b_str = b_value.getText().toString();
                String c_str = c_value.getText().toString();
                String d_str = d_value.getText().toString();
                String y_str = y_value.getText().toString();
                String mut_str = mut_value.getText().toString();

                a_value.setText("");
                b_value.setText("");
                c_value.setText("");
                d_value.setText("");
                y_value.setText("");
                mut_value.setText("");

                try {
                    a=Integer.parseInt(a_str);
                    b=Integer.parseInt(b_str);
                    c=Integer.parseInt(c_str);
                    d=Integer.parseInt(d_str);
                    y=Integer.parseInt(y_str);
                    mut=Double.parseDouble(mut_str);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(),"Incorrect input type!", Toast.LENGTH_LONG).show();
                    return;
                }
                result = Genetic(a, b, c, d, y, mut);
                out_res.setText(a+"*"+result[0]+"+"+b+"*"+result[1]+"+"+c+"*"+result[2]+"+"+d+"*"+result[3]+"="+y);
            }
        });
    }
    public int[] Genetic(int a, int b, int c, int d, int y, double mut) {
        int [][] population = new int [4][4];
        Random rand = new Random();

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                population[i][j] = rand.nextInt(y / 2)+1;
            }
        }

        for(int iter = 0; iter < Integer.MAX_VALUE / 2; iter++){
            int[] val = new int[4];
            double percent = 0.0;

            for (int i = 0; i < population.length; i++){
                val[i] = a*population[i][0] + b*population[i][1] + c*population[i][2] + d*population[i][3];
                int delta = val[i] - y;
                if (delta == 0){
                    return population[i];
                }
                percent = percent + 1 / val[i];
            }

            double[] range = new double[5];
            range[0] = 0.0;
            range[4] = 100.0;

            for(int i = 0; i < range.length - 1; i++){
                range[i+1] = range[i]+(val[i]/percent);
            }

            int[] parent = new int[4];

            for(int i = 0; i < parent.length; i++){
                int id;
                int temp = rand.nextInt(100);
                for(id = 1; id < range.length && range[id] < temp; id++);
                parent[i] = (id == 0) ? id : id -1;
            }
            int[][] children = new int[4][4];
            int x = -1;
            int parent_size = parent.length;
            int children_size = children.length;
            for(int i = 0; i < population.length; i++){
                int p1 = parent[rand.nextInt(parent_size - 1)];
                int p2 = parent[rand.nextInt(parent_size - 1)];
                int threshlod = rand.nextInt(3)+1;
                for (int j = 0; j < children_size; j++){
                    children[i][j] = (j < threshlod) ?  population[p1][j] : population[p2][j];
                }

                if(rand.nextDouble()<mut){
                    int selected = rand.nextInt(children_size);
                    int add_val;
                    if(rand.nextDouble() > 0.5 && children[i][selected] < y /2){
                        add_val = 1;
                    }else if(children[i][selected] > 1){
                        add_val = -1;
                    }else{
                        add_val = 0;
                    }
                    children[i][selected] += add_val;
                }
            }

            population = new int[4][4];
            for(int i = 0; i < children_size; i++){
                for(int j = 0; j < children_size; j++){
                    population[i][j] = children[i][j];
                }
            }
        }

        int min = Integer.MAX_VALUE;
        for(int j = 0; j < population.length; j++){
            int temp = a*population[j][0] + b*population[j][1] + c*population[j][2] + d*population[j][3];
            if((temp - y) < min){
                min = temp;
            }
        }
        return null;
    }
}
