package com.example.treesapv2new;

public class Benefits_Columns_Enum {
    enum database{
        HopeCollegeData, iTreeData, CoHdatabase, ExtendedCoH, AllUserDB, User;
//        private String dataSource;
//
//        database(String dataSource){
//            this.dataSource = dataSource;
//        }
    }

    database db;

    public  Benefits_Columns_Enum(database ds){
        db = ds;
    }


    public int commonNameIndex(){
        switch(db) {
            case HopeCollegeData:
                return 1;
            case iTreeData:
                return 2;
            case CoHdatabase:
                return 2;
            case ExtendedCoH:
                return 1;
            default:
                return -1;
        }

    }

    /// Returns the index for the scientific name, or -1 if there is none

    public int scientificNameIndex() {
        switch(db) {
            case CoHdatabase:
                return 2;
            case iTreeData:
                return 1;
            case HopeCollegeData:
                return 1;
            case ExtendedCoH:
                return -1;
            default:
                return -1;
        }

    }



    /// Returns the index for the latitude, or -1 if there is none

    public int latitudeIndex(){

        switch(db) {
            case CoHdatabase:
                return 15;
            case iTreeData:
                return 4;
            case HopeCollegeData:
                return 4;
            case ExtendedCoH:
                return 4;
            default:
                return -1;
        }
    }



    /// Returns the index for the longitude, or -1 if there is none

    public int longitudeIndex(){
        switch(db) {
            case CoHdatabase:
                return 16;
            case iTreeData:
                return 5;
            case HopeCollegeData:
                return 5;
            case ExtendedCoH:
                return 3;
            default:
                return -1;
        }
    }



    /// Returns the index for the DBH, or -1 if there is none

    public int dbhIndex(){
        switch(db) {
            case CoHdatabase:
                return 3;
            case iTreeData:
                return 10;
            case HopeCollegeData:
                return 10;
            case ExtendedCoH:
                return 2;
            default:
                return -1;
        }

    }



    /// Returns the index for the DBH(1), or -1 if there is none

    public int dbh1Index() {

        switch (db) {

            case CoHdatabase:

                return 11;

            case iTreeData:

                return 11;

            case HopeCollegeData:

                return 11;

            default:

                return -1;

        }

    }



    /// Returns the index for the DBH(2), or -1 if there is none

    public int dbh2Index() {

        switch (db) {

            case CoHdatabase:

                return 12;

            case iTreeData:

                return 12;

            case HopeCollegeData:

                return 12;

            default:

                return -1;

        }

    }



    /// Returns the index for the DBH(3), or -1 if there is none

    public int dbh3Index() {

        switch (db) {

            case CoHdatabase:

                return 14;

            case iTreeData:

                return 13;

            case HopeCollegeData:

                return 13;

            default:

                return -1;

        }

    }



    /// Returns the index for whether the tree is native, or -1 if there is none

    public int nativeIndex() {

        switch (db) {

            case ExtendedCoH:

                return 15;

            default:

                return -1;

        }

    }



    // MARK: - General benefit information



    /// Returns the index for carbon sequestration (lbs/yr), or -1 if there is none

    public int carbonSequestrationPoundsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 19;

            default:

                return -1;

        }

    }



    /// Returns the index for carbon sequestration ($/yr), or -1 if there is none

    public int carbonSequestrationDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 20;

            default:

                return -1;

        }

    }



    /// Returns the index for water untercepted (ft^3/yr), or -1 if there is none

    public int waterInterceptedCubicFeetIndex() {

        switch (db) {

            case ExtendedCoH:

                return 33;

            default:

                return -1;

        }

    }



    /// Returns the index for ainted runoff (ft^3/yr), or -1 if there is none

    public int aintedRunoffCubicFeetIndex() {

        switch (db) {

            case ExtendedCoH:

                return 21;

            default:

                return -1;

        }

    }



    /// Returns the index for ainted runoff ($/yr), or -1 if there is none

    public int aintedRunoffDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 22;

            default:

                return -1;

        }

    }



    /// Returns the index for carbon ainted (lbs/yr), or -1 if there is none

    public int carbonAintedPoundsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 23;

            default:

                return -1;

        }

    }



    /// Returns the index for carbon ainted ($/yr), or -1 if there is none

    public int carbonAintedDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 24;

            default:

                return -1;

        }

    }



    /// Returns the index for pollution removal (oz/yr), or -1 if there is none

    public int pollutionRemovalOuncesIndex() {

        switch (db) {

            case ExtendedCoH:

                return 25;

            default:

                return -1;

        }

    }



    /// Returns the index for pollution removal ($/yr), or -1 if there is none

    public int pollutionRemovalDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 26;

            default:

                return -1;

        }

    }



    /// Returns the index for energy savings ($/yr), or -1 if there is none

    public int energySavingsDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 27;

            default:

                return -1;

        }

    }



    /// Returns the index for total annual ExtendedCoH ($/yr), or -1 if there is none

    public int totalAnnualBenefitsDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 28;

            default:

                return -1;

        }

    }



    // MARK: - Breakdown benefit information



    /// Returns the index for CO ($/yr), or -1 if there is none

    public int coDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 41;

            default:

                return -1;

        }

    }



    /// Returns the index for CO (oz/yr), or -1 if there is none

    public int coOuncesIndex() {

        switch (db) {

            case ExtendedCoH:

                return 36;

            default:

                return -1;

        }

    }



    /// Returns the index for O3 ($/yr), or -1 if there is none

    public int o3DollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 42;

            default:

                return -1;

        }

    }



    /// Returns the index for O3 (oz/yr), or -1 if there is none

    public int o3OuncesIndex() {

        switch (db) {

            case ExtendedCoH:

                return 37;

            default:

                return -1;

        }

    }



    /// Returns the index for NO2 ($/yr), or -1 if there is none

    public int no2DollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 43;

            default:

                return -1;

        }

    }



    /// Returns the index for NO2 (oz/yr), or -1 if there is none

    public int no2OuncesIndex() {

        switch (db) {

            case ExtendedCoH:

                return 38;

            default:

                return -1;

        }

    }



    /// Returns the index for SO2 ($/yr), or -1 if there is none

    public int so2DollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 44;

            default:

                return -1;

        }

    }



    /// Returns the index for SO2 (oz/yr), or -1 if there is none

    public int so2OuncesIndex() {

        switch (db) {

            case ExtendedCoH:

                return 39;

            default:

                return -1;

        }

    }



    /// Returns the index for PM25 ($/yr), or -1 if there is none

    public int pm25DollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 45;

            default:

                return -1;

        }

    }



    /// Returns the index for PM25 (oz/yr), or -1 if there is none

    public int pm25OuncesIndex() {

        switch (db) {

            case ExtendedCoH:

                return 40;

            default:

                return -1;

        }

    }



    /// Returns the index for heating(1) ($/yr), or -1 if there is none

    public int heating1DollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 54;

            default:

                return -1;

        }

    }



    /// Returns the index for heating(2) ($/yr), or -1 if there is none

    public int heating2DollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 56;

            default:

                return -1;

        }

    }



    /// Returns the index for heating (MBTU/yr), or -1 if there is none

    public int heatingMBTUIndex() {

        switch (db) {

            case ExtendedCoH:

                return 53;

            default:

                return -1;

        }

    }



    /// Returns the index for cooling ($/yr), or -1 if there is none

    public int coolingDollarsIndex() {

        switch (db) {

            case ExtendedCoH:

                return 58;

            default:

                return -1;

        }

    }



    /// Returns the index for cooling ($/yr), or -1 if there is none

    public int coolingKWHIndex() {

        switch (db) {

            case ExtendedCoH:

                return 57;

            default:

                return -1;

        }

    }

}
