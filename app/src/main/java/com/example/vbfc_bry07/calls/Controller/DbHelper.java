package com.example.vbfc_bry07.calls.Controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static String CREATED_AT = "created_at",
            UPDATED_AT = "updated_at",
            DELETED_AT = "deleted_at",
            AI_ID = "id";


    public DbHelper(Context context) {
        super(context, "ECE_calls", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlansController.CREATE_PLANS);
        db.execSQL(DoctorsController.CREATE_DOCTORS);
        db.execSQL(SpecializationsController.CREATE_SPECIALIZATIONS);

        db.execSQL(BroadcastsController.CREATE_Broadcasts);
        db.execSQL(CallDetailingAidsController.CREATE_CallDetailingAids);
        db.execSQL(CallMaterialsController.CREATE_CallMaterials);
        db.execSQL(CallNotesController.CREATE_CallNotes);
        db.execSQL(CallReportsController.CREATE_CallReports);
        db.execSQL(CallsController.CREATE_Calls);
        db.execSQL(CycleDaysController.CREATE_CycleDays);
        // db.execSQL(CycleSetsController.CREATE_CycleSets);
        // db.execSQL(DayTypesController.CREATE_DayTypes);
        // db.execSQL(DetailingAidEmailsController.CREATE_DetailingAidEmails);
        // db.execSQL(DetailingAids.CREATE_DetailingAids);
//         db.execSQL(DoctorClasses.CREATE_DoctorClasses);

        db.execSQL(InstitutionDoctorMapsController.CREATE_InstitutionDoctorMaps);
        db.execSQL(InstitutionsController.CREATE_Institutions);
        db.execSQL(MaterialAllocationsController.CREATE_MaterialAllocation);
        db.execSQL(MaterialInventoriesController.CREATE_MaterialInventories);
        db.execSQL(MaterialReplenishmentDetailsController.CREATE_MaterialReplenishmentDetails);
        db.execSQL(MaterialReplenishmentsController.CREATE_MaterialReplenishments);
        db.execSQL(MaterialsController.CREATE_Materials);
        db.execSQL(MaterialsByClassSpecializationMapsController.CREATE_MaterialsByClassSpecializationMaps);
        db.execSQL(MaterialsByInstitutionDoctorMapsController.CREATE_MaterialsByInstitutionDoctorMaps);
        db.execSQL(MissedCallsController.CREATE_MissedCalls);
        db.execSQL(MockPlanDetailsController.CREATE_MockPlanDetails);
        db.execSQL(MockPlansController.CREATE_MockPlans);
        db.execSQL(ModulesController.CREATE_Modules);
        db.execSQL(PlanDetailsController.CREATE_PlanDetails);
        db.execSQL(PreferencesController.CREATE_PREFERENCES);
        db.execSQL(ProductsController.CREATE_Products);
        db.execSQL(QuickSignaturesController.CREATE_QuickSignatures);
        db.execSQL(ReasonsController.CREATE_Reasons);
        db.execSQL(RescheduledCallsController.CREATE_RescheduledCalls);
        db.execSQL(SalesReportDetailsController.CREATE_SalesReportDetails);
        db.execSQL(SalesReportsController.CREATE_SalesReports);
        db.execSQL(SettingsController.CREATE_Settings);
        db.execSQL(SignaturesController.CREATE_Signatures);
        db.execSQL(StagesController.CREATE_Stages);
        db.execSQL(VersionsController.CREATE_Versions);

        PreferencesController.insertToTablePreferences(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS ECE_calls";
        db.execSQL(sql);
    }


}
