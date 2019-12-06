package com.maihuythong.testlogin.rate_comment_review;

public class GetPointOfTour {
    private Point[] pointStats;

    public Point[] getPointStats() {
        return pointStats;
    }

    public void setPointStat(Point[] pointStats) {
        this.pointStats = pointStats;
    }


    class Point {
        private int point;
        private int total;

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
