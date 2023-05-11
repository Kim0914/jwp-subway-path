package subway.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Line {

    private Long id;
    private String name;
    private LinkedList<Section> sections;

    public Line(Long id, String name, LinkedList<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public boolean hasLeftStationInSection(Station station) {
        return sections.stream()
                .filter(section -> section.getLeft().equals(station))
                .count() > 0;
    }

    public boolean hasRightStationInSection(Station station) {
        return sections.stream()
                .filter(section -> section.getRight().equals(station))
                .count() > 0;
    }

    public Section findSectionByLeftStation(Station station) {
        return sections.stream()
                .filter(section -> section.getLeft().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }

    public Section findSectionByRightStation(Station station) {
        return sections.stream()
                .filter(section -> section.getRight().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }

    public boolean hasStation(Station station) {
        for (Section section : sections) {
            Station leftStation = section.getLeft();
            Station rightStation = section.getRight();

            if (station.equals(leftStation) || station.equals(rightStation)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOneSection() {
        return sections.size() == 1;
    }

    public boolean isLastStationAtLeft(Station station) {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        leftStations.removeAll(rightStations);

        return leftStations.stream()
                .filter(lettStation -> lettStation.equals(station))
                .count() == 1;
    }

    public boolean isLastStationAtRight(Station station) {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        rightStations.removeAll(leftStations);

        return rightStations.stream()
                .filter(rightStation -> rightStation.equals(station))
                .count() == 1;
    }

    public List<Station> findLeftToRightRoute() {
        Station lastStations = findLastStationAtLeft();
        List<Station> stations = new ArrayList<>(List.of(lastStations));

        Station targetStation = lastStations;
        while (stations.size() < sections.size() + 1) {
            for (Section section : sections) {
                if (section.getLeft().equals(targetStation)) {
                    Station nextStation = section.getRight();
                    stations.add(nextStation);
                    targetStation = nextStation;
                    break;
                }
            }
        }
        return stations;
    }

    private Station findLastStationAtLeft() {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        leftStations.removeAll(rightStations);

        return leftStations.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("종점을 찾을 수 없습니다."));
    }

    private Station findLastStationAtRight() {
        List<Station> leftStations = findLeftStations();
        List<Station> rightStations = findRightStations();

        rightStations.removeAll(leftStations);

        return rightStations.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("종점을 찾을 수 없습니다."));
    }

    private List<Station> findLeftStations() {
        List<Station> leftStations = sections.stream()
                .map(Section::getLeft)
                .collect(Collectors.toList());
        return leftStations;
    }

    private List<Station> findRightStations() {
        List<Station> rightStations = sections.stream()
                .map(Section::getRight)
                .collect(Collectors.toList());
        return rightStations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(getId(), line.getId()) && Objects.equals(getName(), line.getName())
                && Objects.equals(getSections(), line.getSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSections());
    }
}
