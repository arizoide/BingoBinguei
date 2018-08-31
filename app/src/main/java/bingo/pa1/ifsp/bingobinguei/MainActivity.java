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
Utilizar imagem - OK PARA ÍCONE
Coordenar letra e número - FEITO
Não sortear a mesma bola duas vezes. - DEITO
Bônus, opção de escolher se repete bolinha ou nao. - ACHEI RUIM, NÃO FIZ
Colocar Github - OK
 */
public class MainActivity extends AppCompatActivity {
    //Constantes
    private final String NUMEROS_SORTEADOS = "numerosSorteados";
    private final String ULTIMO_NUMERO_SORTEADO = "ultimoNumeroSorteado";
    private final String ULTIMO_COLUNA_SORTEADA = "ultimaColunaSorteada";

    //Random para sorteio de número do bingo
    private Random sorteador;

    private TextView numeroSorteadoTextView;

    private Button sortearPedraButton;

    private ArrayList<Integer> numerosSorteados = new ArrayList<>();

    private int numeroSorteado;

    private String colunaSorteada;

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
            do {
                numeroSorteado = sorteador.nextInt(74) + 1;
            } while (jaSorteado(numeroSorteado));

            //Adiciona na lista de números que já foram sorteados
            numerosSorteados.add(numeroSorteado);

            colunaSorteada = getColunaSorteada(numeroSorteado);

            numeroSorteadoTextView.setText(colunaSorteada + "-" + numeroSorteado);

            if (numerosSorteados.size() >= 75) {
                sortearPedraButton.setClickable(false);
                sortearPedraButton.setText(R.string.msg_finalizacao);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Salvar os dados de estado dinâmico
        outState.putIntegerArrayList(NUMEROS_SORTEADOS, numerosSorteados);
        outState.putInt(ULTIMO_NUMERO_SORTEADO, numeroSorteado);
        outState.putString(ULTIMO_COLUNA_SORTEADA, colunaSorteada);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            //Recupera a lista de números sorteados
            ArrayList<Integer> numerosSorteadosRestored = savedInstanceState.getIntegerArrayList(NUMEROS_SORTEADOS);
            if (numerosSorteadosRestored != null)
                numerosSorteados.addAll(numerosSorteadosRestored);

            //Recupera o ultimo número sorteado com a coluna
            Integer ultimoNumero = savedInstanceState.getInt(ULTIMO_NUMERO_SORTEADO, 0);
            String ultimaColuna= savedInstanceState.getString(ULTIMO_COLUNA_SORTEADA, null);
            if(ultimoNumero != null && ultimoNumero != 0 && ultimaColuna != null) {
                if (!numeroSorteadoTextView.getText().equals(R.string.msg_finalizacao)) {
                    numeroSorteadoTextView.setText(ultimaColuna + " - " + ultimoNumero);
                } else {
                    numeroSorteadoTextView.setText(R.string.msg_finalizacao);
                }
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
