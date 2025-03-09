package br.com.dbserver.desafio_votacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException e) {

        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage())
                );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<Map<String, String>> handleCpfJaCadastradoException(CpfJaCadastradoException e){

        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(VotacaoForaDaSessaoException.class)
    public ResponseEntity<Map<String, String>> handleVotacaoForaDaSessaoException(VotacaoForaDaSessaoException e){
        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(VotacaoJaFeitaComEsseCpfException.class)
    public ResponseEntity<Map<String, String>> handleVotacaoJaFeitaComEsseCpfException(VotacaoJaFeitaComEsseCpfException e){
        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e){
        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SessaoVotacaoDaPautaJaExisteException.class)
    public ResponseEntity<Map<String, String>> handleSessaoVotacaoDaPautaJaExisteException(SessaoVotacaoDaPautaJaExisteException e){
        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CPFUnableToVoteException.class)
    public ResponseEntity<Map<String, String>> handleCPFUnableToVoteException(CPFUnableToVoteException e){
        Map<String, String> response = new HashMap<>();
        response.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
