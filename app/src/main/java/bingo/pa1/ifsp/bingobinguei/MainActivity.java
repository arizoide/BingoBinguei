package bingo.pa1.ifsp.bingobinguei;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    private final String CARTELA_USUARIO = "cartelaUsuario";
    private final String ULTIMO_NUMERO_SORTEADO = "ultimoNumeroSorteado";
    private final String ULTIMO_COLUNA_SORTEADA = "ultimaColunaSorteada";
    private final String TEXTO_BOTAO_SORTEAR = "textoBotaoSortear";
    private final int tamanhoCartela = 24;

    //Random para sorteio de número do bingo
    private Random sorteador;

    //Views
    private ImageView colunaSorteadaImageView;

    private TextView numeroSorteadoTextView;
    private TextView cartelaSorteadaTextView;
    private TextView numeroSorteadoGrandeTextView;

    private Button sortearPedraButton;

    private ArrayList<Integer> numerosSorteados = new ArrayList<>();
    private ArrayList<Integer> cartelaUsuario = new ArrayList<>();

    private int numeroSorteado;
    private String colunaSorteada;
    private String msgBotaoSortear = "Sortear Próxima Pedra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Após criação da tela
        sorteador = new Random(System.currentTimeMillis());

        //Recuperando referencia para o text view da tela.
        numeroSorteadoTextView = findViewById(R.id.numeroSorteadoTextView);
        cartelaSorteadaTextView = findViewById(R.id.cartelaSorteadaTextView);
        numeroSorteadoGrandeTextView = findViewById(R.id.numeroSorteadoGrandeTextView);

        //Recuperando referencia para a imagem da tela
        colunaSorteadaImageView = findViewById(R.id.colunaSorteadaImageView);

        //Referencia dos botões da tela
        sortearPedraButton = findViewById(R.id.sortearPedraButton);


        //Gera uma cartela randomica para o usuário participar do bingo se ainda não gerou
        //Gera 24 números, pois o 25 é o coringa =D
        if (savedInstanceState != null) {
            cartelaUsuario = savedInstanceState.getIntegerArrayList(CARTELA_USUARIO);
            if (cartelaUsuario == null) {
                cartelaUsuario.addAll(geraCartelaRandomica());
            }

            cartelaSorteadaTextView.setText(getCartelaUnificada());

            //Valida se o botao sortear deveria ter um texto diferente
            msgBotaoSortear = savedInstanceState.getString(TEXTO_BOTAO_SORTEAR);
        } else {
            cartelaUsuario.addAll(geraCartelaRandomica());
            cartelaSorteadaTextView.setText(getCartelaUnificada());
        }

        sortearPedraButton.setText(msgBotaoSortear);
    }

    //ONCLICK do botao de sortear numero
    public void sortearProximaPedra(View view) {
        if (view.getId() == R.id.sortearPedraButton) {
            do {
                numeroSorteado = sorteador.nextInt(74) + 1;
            } while (jaSorteado(numeroSorteado, numerosSorteados));

            //Adiciona na lista de números que já foram sorteados
            numerosSorteados.add(numeroSorteado);

            //Recupera a coluna sorteada e já seta a imagem da coluna.
            colunaSorteada = getColunaSorteada(numeroSorteado);

            numeroSorteadoTextView.setText(colunaSorteada + "-" + numeroSorteado);
            numeroSorteadoGrandeTextView.setText(String.valueOf(numeroSorteado));

            //Verifica se usuario completou a cartela já!
            if (usuarioCompletouCartela()) {
                sortearPedraButton.setClickable(false);
                sortearPedraButton.setText(R.string.msg_bingo);

                //Inseringo 'alert' para exibir que o usuario ganhou, testando conceito aprendendo na aula.
                Toast.makeText(this, "Parabéns! Você ganhou a partida!!!!", Toast.LENGTH_LONG).show();
                msgBotaoSortear = getString(R.string.msg_bingo);
            }

            //Valida se ainda existem números para ser sorteados.
            if (numerosSorteados.size() >= 75) {
                sortearPedraButton.setClickable(false);
                sortearPedraButton.setText(R.string.msg_finalizacao);
                msgBotaoSortear = getString(R.string.msg_finalizacao);
            }
        }
    }

    //SALVAR A INSTANCIA DO OBJETO
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Salvar os dados de estado dinâmico
        outState.putIntegerArrayList(NUMEROS_SORTEADOS, numerosSorteados);
        outState.putInt(ULTIMO_NUMERO_SORTEADO, numeroSorteado);
        outState.putString(ULTIMO_COLUNA_SORTEADA, colunaSorteada);
        outState.putIntegerArrayList(CARTELA_USUARIO, cartelaUsuario);
        outState.putString(TEXTO_BOTAO_SORTEAR, msgBotaoSortear);
    }

    //Recupera alguns dados salvos
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
            String ultimaColuna = savedInstanceState.getString(ULTIMO_COLUNA_SORTEADA, null);
            if (ultimoNumero != 0 && ultimaColuna != null) {
                if (!numeroSorteadoTextView.getText().equals(R.string.msg_finalizacao)) {
                    numeroSorteado = ultimoNumero;
                    colunaSorteada = ultimaColuna;
                    numeroSorteadoTextView.setText(ultimaColuna + " - " + ultimoNumero);
                    getColunaSorteada(numeroSorteado);
                    numeroSorteadoGrandeTextView.setText(String.valueOf(ultimoNumero));
                } else {
                    numeroSorteadoTextView.setText(R.string.msg_finalizacao);
                }
            }
        }
    }

    //========================= MÉTODOS PRIVADOS ==========================

    // Método que verifica se um número já foi sorteado
    private boolean jaSorteado(int numeroSorteado, ArrayList<Integer> numerosSorteadosValidar) {
        for (int numero : numerosSorteadosValidar) {
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
            colunaSorteadaImageView.setImageResource(R.drawable.b);
        } else if (numeroSorteado <= 30) {
            colunaSorteada = "I";
            colunaSorteadaImageView.setImageResource(R.drawable.i);
        } else if (numeroSorteado <= 45) {
            colunaSorteada = "N";
            colunaSorteadaImageView.setImageResource(R.drawable.n);
        } else if (numeroSorteado <= 60) {
            colunaSorteada = "G";
            colunaSorteadaImageView.setImageResource(R.drawable.g);
        } else {
            colunaSorteada = "O";
            colunaSorteadaImageView.setImageResource(R.drawable.o);
        }
        return colunaSorteada;
    }

    //Método para gerar uma cartela para o usuario randomicamente
    private ArrayList<Integer> geraCartelaRandomica() {
        ArrayList<Integer> cartela = new ArrayList<>();
        for (int i = 0; i < tamanhoCartela; i++) {
            int sorteado;
            do {
                sorteado = sorteador.nextInt(74) + 1;
            } while (jaSorteado(sorteado, cartela));

            cartela.add(sorteado);
        }

        return cartela;
    }

    //Recupera os numeros sorteados da cartela do usuario em uma unica string
    private String getCartelaUnificada() {
        String unificada = "";
        int count = 0;

        Collections.sort(cartelaUsuario);
        for (int numero : cartelaUsuario) {
            if (count < 23) {
                unificada += numero + " - ";
                count++;
            } else {
                unificada += numero;
            }
        }

        return unificada;
    }

    //Verifica se o usuario completou o bingo
    private boolean usuarioCompletouCartela() {
        int countBingo = 0;
        for (int sorteado : numerosSorteados) {
            for (int numeroCartela : cartelaUsuario) {
                if (numeroCartela == sorteado) {
                    countBingo++;
                }
            }
        }

        if (countBingo == 24)
            return true;

        return false;
    }
}
