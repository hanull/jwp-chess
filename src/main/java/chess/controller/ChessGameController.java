package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.CreateGameDto;
import chess.dto.MovePositionDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("game", HttpStatus.OK);
    }

    @PostMapping("/game")
    public ResponseEntity<ChessGameDto> createNewGame(@RequestBody CreateGameDto createGameDto) {
        final ChessGameDto newChessGame = chessGameService.createNewChessGame(createGameDto);
        return ResponseEntity.ok(newChessGame);
    }

    @GetMapping("/status")
    public ResponseEntity<StatusDto> findStatusByGameName(@RequestParam("name") String gameName) {
        final StatusDto status = chessGameService.findStatus(gameName);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/game")
    public ResponseEntity<Void> finishGame(@RequestParam("name") String gameName) {
        chessGameService.finishGame(gameName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/load")
    public ResponseEntity<ChessGameDto> loadGame(@RequestParam("name") String gameName) {
        final ChessGameDto loadChessGame = chessGameService.loadChessGame(gameName);
        return ResponseEntity.ok(loadChessGame);
    }

    @PostMapping("/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MovePositionDto movePositionDto) {
        final String chessGameName = movePositionDto.getChessGameName();
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(chessGameName, currentPosition, destinationPosition);
        return ResponseEntity.ok(chessGame);
    }
}
