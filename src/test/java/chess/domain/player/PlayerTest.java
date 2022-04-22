package chess.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.position.Position;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    @DisplayName("플레이어가 특정 체스말을 가지고 있는지 확인한다.")
    void checkHasPiece() {
        Piece piece1 = new Pawn(new Position(2, 'a'));
        Piece piece2 = new Pawn(new Position(3, 'b'));
        Player player = new Player(List.of(piece1, piece2), Team.WHITE);

        assertThat(player.hasPiece(new Position(3, 'b'))).isTrue();
    }

    @Test
    @DisplayName("플레이어가 가진 모든 체스말을 찾는다.")
    void findAllPiece() {
        final Piece piece1 = new Pawn(new Position(2, 'b'));
        final Piece piece2 = new Rook(new Position(1, 'a'));
        final Piece piece3 = new King(new Position(1, 'e'));
        final Player player = new Player(List.of(piece1, piece2, piece3), Team.WHITE);

        assertThat(player.findAll()).contains(piece1, piece2, piece3);
    }

    @Test
    @DisplayName("플레이어가 체스말을 이동시켰는지 확인한다.")
    void checkMovePiece() {
        final Position currentPosition = new Position(2, 'a');
        final Position expected = new Position(4, 'a');
        final Piece piece = new Pawn(currentPosition);
        final Player player = new Player(List.of(piece), Team.WHITE);

        player.move(currentPosition, expected);
        final List<Piece> pieces = player.findAll();

        assertThat(pieces).contains(new Pawn(expected));
    }

    @Test
    @DisplayName("플레이어가 체스말을 capture 했는지 확인한다.")
    void capturePiece() {
        final Position currentPosition = new Position(2, 'a');
        final Position expected = new Position(3, 'b');
        final Piece piece = new Pawn(currentPosition);
        final Player player = new Player(List.of(piece), Team.WHITE);

        player.capture(currentPosition, expected);
        final List<Piece> pieces = player.findAll();

        assertThat(pieces).contains(new Pawn(expected));
    }

    @Test
    @DisplayName("체스말을 삭제할 수 있다. 삭제할 수 있는 체스말이 없다면 예외가 발생한다.")
    void removePiece() {
        final Position position = new Position(2, 'a');
        final Piece piece = new Pawn(position);
        final Player player = new Player(List.of(piece), Team.WHITE);

        player.remove(position);

        assertThatThrownBy(() -> player.remove(position))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("체스말이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("플레이어의 점수를 구한다.")
    void calculateScore() {
        final Piece piece1 = new Pawn(new Position(2, 'a'));
        final Piece piece3 = new Pawn(new Position(4, 'b'));
        final Piece piece2 = new Pawn(new Position(4, 'a'));
        final Player player = new Player(List.of(piece1, piece2, piece3), Team.WHITE);
        final double expected = 2;

        final double actual = player.calculateScore().getScore();

        assertThat(actual).isEqualTo(expected);
    }
}
