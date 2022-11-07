package org.kjs.pola.model;

import java.math.BigDecimal;

public class QtyCost
{
    public BigDecimal Qty;
    public BigDecimal Cost;
    
    public QtyCost(final BigDecimal qty, final BigDecimal cost) {
        this.Qty = null;
        this.Cost = null;
        this.Qty = qty;
        this.Cost = cost;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Qty=").append(this.Qty).append(",Cost=").append(this.Cost);
        return sb.toString();
    }
}
