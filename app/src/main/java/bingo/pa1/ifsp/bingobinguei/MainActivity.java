package bingo.pa1.ifsp.bingobinguei;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


/*
Postar até sexta no Moodle. Sorteio de bingo aplicativo
Utilizar imagem
Coordenar letra e número
Não sortear a mesma bola duas vezes.
Bônus, opção de escolher se repete bolinha ou nao.
Colocar Github
 */
public class MainActivity extends AppCompatActivity {
    //Random para sorteio de número do bingo
    private Random sorteador;

    private TextView numeroSorteadoTextView;

    private Button sortearPedraButton;

    private ArrayList<Integer> numerosSorteados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Após criação da tela
        sorteador = new Random(System.currentTimeMillis());

        //Recuperando referencia para o text view da tela.
        numeroSorteadoTextView = findViewById(R.id.numeroSorteadoTextView);

        //Referencia dos botões da tela
        sortearPedraButton = findViewById(R.id.sortearPedraButton);
    }

    //ONCLICK do botao de sortear numero
    public void sortearProximaPedra(View view) {
        if (view.getId() == R.id.sortearPedraButton) {
            int numeroSorteado;

            do {
                numeroSorteado = sorteador.nextInt(74) + 1;
            } while (jaSorteado(numeroSorteado));

            numerosSorteados.add(numeroSorteado);

            String colunaSorteada = getColunaSorteada(numeroSorteado);

            numeroSorteadoTextView.setText(colunaSorteada + "-" + numeroSorteado);

            if (numerosSorteados.size() >= 75) {
                sortearPedraButton.setClickable(false);
                sortearPedraButton.setText("Todos os números já sorteados!");
            }
        }
    }

    //=====================================================================
    //=====================================================================
    //========================= MÉTODOS PRIVADOS ==========================
    //=====================================================================
    //=====================================================================

    // Método que verifica se um número já foi sorteado
    private boolean jaSorteado(int numeroSorteado) {
        for (int numero : numerosSorteados) {
            if (numeroSorteado == numero) {
                return true;
            }
        }
        return false;
    }

    //Método que valida a coluna que foi sorteada, entre as opções B-I-N-G-O
    @NonNull
    private String getColunaSorteada(int numeroSorteado) {
        String colunaSorteada;

        if (numeroSorteado <= 15) {
            colunaSorteada = "B";
        } else if (numeroSorteado <= 30) {
            colunaSorteada = "I";
        } else if (numeroSorteado <= 45) {
            colunaSorteada = "N";
        } else if (numeroSorteado <= 60) {
            colunaSorteada = "G";
        } else {
            colunaSorteada = "O";
        }
        return colunaSorteada;
    }
}
