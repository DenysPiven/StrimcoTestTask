package org.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Player {
    private String name;
    private String nation;
    private List<Position> positions;
    private String club;
    private Integer age;

    private Double GKRating;
    private Double DLRating;
    private Double DCRating;
    private Double DRRating;
    private Double DMRating;
    private Double MLRating;
    private Double MCRating;
    private Double MRRating;
    private Double AMLRating;
    private Double AMCRating;
    private Double AMRRating;
    private Double FSRating;
    private Double TSRating;

    public Player(String name, String nation, String positionStr, String club, Integer age,
                  Double GKRating, Double DLRating, Double DCRating, Double DRRating,
                  Double DMRating, Double MLRating, Double MCRating, Double MRRating,
                  Double AMLRating, Double AMCRating, Double AMRRating,
                  Double FSRating, Double TSRating) {
        this.name = name;
        this.nation = nation;
        this.positions = parsePositions(positionStr);
        this.club = club;
        this.age = age;
        this.GKRating = GKRating;
        this.DLRating = DLRating;
        this.DCRating = DCRating;
        this.DRRating = DRRating;
        this.DMRating = DMRating;
        this.MLRating = MLRating;
        this.MCRating = MCRating;
        this.MRRating = MRRating;
        this.AMLRating = AMLRating;
        this.AMCRating = AMCRating;
        this.AMRRating = AMRRating;
        this.FSRating = FSRating;
        this.TSRating = TSRating;
    }

    public String getPrimaryNation() {
        if (this.nation == null) return null;
        String[] parts = this.nation.split("/");
        return parts[0].trim();
    }

    public Double getSTRating() {
        return Math.max(
                this.FSRating != null ? this.FSRating : 0.0,
                this.TSRating != null ? this.TSRating : 0.0
        );
    }

    public Double getRatingForPosition(Position pos) {
        if (pos == null) return 0.0;
        switch (pos) {
            case GK: return GKRating != null ? GKRating : 0.0;
            case DL: return DLRating != null ? DLRating : 0.0;
            case DC: return DCRating != null ? DCRating : 0.0;
            case DR: return DRRating != null ? DRRating : 0.0;
            case DM: return DMRating != null ? DMRating : 0.0;
            case ML: return MLRating != null ? MLRating : 0.0;
            case MC: return MCRating != null ? MCRating : 0.0;
            case MR: return MRRating != null ? MRRating : 0.0;
            case AML: return AMLRating != null ? AMLRating : 0.0;
            case AMC: return AMCRating != null ? AMCRating : 0.0;
            case AMR: return AMRRating != null ? AMRRating : 0.0;
            case FC:
            case ST:
                return getSTRating();
            default: return 0.0;
        }
    }

    private List<Position> parsePositions(String positionStr) {
        List<Position> result = new ArrayList<>();
        if (positionStr == null || positionStr.isEmpty()) {
            return result;
        }

        String[] parts = positionStr.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            String[] subParts = trimmed.split("\\s+");
            if (subParts.length == 1) {
                Position pos = stringToPosition(subParts[0]);
                if (pos != null) result.add(pos);
            } else {
                String base = subParts[0];
                for (int i = 1; i < subParts.length; i++) {
                    String code = subParts[i].toUpperCase();
                    for (char c : code.toCharArray()) {
                        String posStr = base + c;
                        Position pos = stringToPosition(posStr);
                        if (pos != null) result.add(pos);
                    }
                }
            }
        }

        return result;
    }

    private Position stringToPosition(String posStr) {
        posStr = posStr.toUpperCase();
        try {
            return Position.valueOf(posStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
