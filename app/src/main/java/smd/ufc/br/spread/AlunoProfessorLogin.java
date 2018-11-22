package smd.ufc.br.spread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlunoProfessorLogin extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_professor_login);

        Button btnProfessor, btnAluno;
        btnAluno = findViewById(R.id.btn_aluno);
        btnProfessor = findViewById(R.id.btn_professor);

        btnAluno.setOnClickListener(this);
        btnProfessor.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent();
        switch(view.getId()){
            case R.id.btn_aluno:
                i = new Intent(this, LoginAlunoActivity.class);
                break;
            case R.id.btn_professor:
                i = new Intent(this, LoginProfessorActivity.class);
                break;
        }
        startActivityForResult(i, 789);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            setResult(resultCode);
            //startActivity(new Intent(this, MainActivity.class));
            finish();

    }
}
