package org.example.expert.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    // 예상대로 성공했는지
    @Test
    @DisplayName("메서드가 정상적으로 동작한다")
    void matches_ok() {
        // given
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        // then
        assertThat(matches).isTrue();

    }


}
