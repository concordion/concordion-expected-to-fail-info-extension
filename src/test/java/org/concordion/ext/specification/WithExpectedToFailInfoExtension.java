package org.concordion.ext.specification;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.expectedtofailinfo.ExpectedToFailInfoExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;


@RunWith(ConcordionRunner.class)
@Extensions({ExpectedToFailInfoExtension.class})
public class WithExpectedToFailInfoExtension {

    private int totalAmount = 0;
    private int totalSpent = 0;

    public void amountTotal(String amount) {
        totalAmount = Integer.parseInt(amount.trim());
    }

    public void amountSpent(String spent) {
        totalSpent = Integer.parseInt(spent.trim());
    }

    public int calculateRemaining() {
        return (totalAmount - totalSpent);
    }

}
