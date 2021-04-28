package chess.api;

import chess.domain.User;
import chess.domain.piece.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessRoomDto;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class ChessApiController {

    private final ChessGameService chessGameService;

    @Autowired
    private User user;

    public ChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/chessgames/{roomId}/pieces")
    public ResponseEntity<ChessGameDto> findPieces(@PathVariable long roomId,
                                                   @RequestParam String source,
                                                   @RequestParam String target) {
        Position sourcePosition = Position.parseChessPosition(source);
        Position targetPosition = Position.parseChessPosition(target);
        ChessGameDto chessGameDto = chessGameService.moveChessPiece(sourcePosition, targetPosition, roomId);
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/chessgames/{roomId}")
    public ResponseEntity<ChessGameDto> findChessGame(@PathVariable long roomId) {
        ChessGameDto chessGame = chessGameService.findChessGame(roomId);
        return ResponseEntity.ok(chessGame);
    }

    @PostMapping("/chessgames")
    public ResponseEntity<ChessRoomDto> newRoom() {
        ChessRoomDto chessRoomDto = chessGameService.createNewChessRoom();
        user.addRoomId(chessRoomDto.getRoomId());
        URI uri = UriComponentsBuilder
                .fromUriString("/chessgames/{roomId}")
                .buildAndExpand(chessRoomDto.getRoomId())
                .toUri();
        return ResponseEntity.created(uri).body(chessRoomDto);
    }

    @DeleteMapping("/chessgames/{roomId}")
    public ResponseEntity<ChessGameDto> endChessGame(@PathVariable long roomId) {
        if (user.hasRoomId(roomId)) {
            chessGameService.endGame(roomId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/chessgames/{roomId}/scores")
    public ResponseEntity<ScoreDto> calculateScores(@PathVariable long roomId) {
        ScoreDto scoreDto = chessGameService.calculateScores(roomId);
        return ResponseEntity.ok(scoreDto);
    }

}