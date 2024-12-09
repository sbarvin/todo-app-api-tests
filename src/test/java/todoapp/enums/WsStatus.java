package todoapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WsStatus {
    CLOSE_NORMAL(1000),
    CLOSE_ABNORMAL(1006);

    private final int value;
}
