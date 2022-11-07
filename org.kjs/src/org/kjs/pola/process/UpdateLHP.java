package org.kjs.pola.process;

import org.compiere.process.*;
import org.compiere.model.*;
import org.compiere.util.*;
import java.util.logging.*;
import java.sql.*;

public class UpdateLHP extends SvrProcess
{
    private int M_Production_ID;
    
    public UpdateLHP() {
        this.M_Production_ID = 0;
    }
    
    protected void prepare() {
        this.M_Production_ID = this.getRecord_ID();
    }
    
    protected String doIt() throws Exception {
        final MProduction LHP = new MProduction(this.getCtx(), this.M_Production_ID, this.get_TrxName());
        final StringBuilder SQLUpdateLHP = new StringBuilder();
        SQLUpdateLHP.append("select f_create_lhp(?,?,?)");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement)DB.prepareStatement(SQLUpdateLHP.toString(), (String)null);
            pstmt.setInt(1, LHP.getM_Production_ID());
            pstmt.setInt(2, LHP.get_ValueAsInt("KJS_ProductionPlan_ID"));
            pstmt.setInt(3, LHP.get_ValueAsInt("KJS_ProductionPlanLine_ID"));
            rs = pstmt.executeQuery();
            while (rs.next()) {}
        }
        catch (SQLException err) {
            this.log.log(Level.SEVERE, SQLUpdateLHP.toString(), (Throwable)err);
            return "";
        }
        finally {
            DB.close(rs, (Statement)pstmt);
            rs = null;
            pstmt = null;
        }
        DB.close(rs, (Statement)pstmt);
        rs = null;
        pstmt = null;
        return "";
    }
}