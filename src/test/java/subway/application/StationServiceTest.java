package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.StationRequest;
import subway.controller.dto.StationResponse;
import subway.domain.Station;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    StationService stationService;

    @Test
    @DisplayName("역을 추가한다.")
    void saveStation() {
        StationRequest stationRequest = new StationRequest("잠실역");
        StationResponse expectedResponse = new StationResponse(1L, "잠실역");
        given(stationRepository.save(any()))
                .willReturn(new Station(1L, "잠실역"));

        StationResponse response = stationService.saveStation(stationRequest);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(expectedResponse.getId()),
                () -> assertThat(response.getName()).isEqualTo(expectedResponse.getName())
        );
    }

    @Test
    @DisplayName("ID에 해당하는 역 정보를 조회한다.")
    void findStationResponseById() {
        Long id = 1L;
        StationResponse expectedResponse = new StationResponse(id, "잠실역");
        given(stationRepository.findById(any()))
                .willReturn(new Station(1L, "잠실역"));

        StationResponse response = stationService.findStationResponseById(id);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(expectedResponse.getId()),
                () -> assertThat(response.getName()).isEqualTo(expectedResponse.getName())
        );
    }

    @Test
    @DisplayName("모든 역 정보를 조회한다.")
    void findAllStationResponses() {
        List<Station> stations = new ArrayList<>(List.of(new Station(1L, "잠실역"), new Station(2L, "선릉역")));
        given(stationRepository.findAll())
                .willReturn(stations);

        List<StationResponse> responses = stationService.findAllStationResponses();

        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("ID에 해당하는 역을 삭제한다.")
    void deleteStationById() {
        willDoNothing().given(stationRepository).deleteById(any());

        assertDoesNotThrow(() -> stationService.deleteStationById(1L));
    }
}
