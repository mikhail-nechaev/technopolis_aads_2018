package ru.mail.polis.collections;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.mail.polis.collections.task.T10_AVL;
import ru.mail.polis.collections.task.T11_AVLI;
import ru.mail.polis.collections.task.T12_AVLIR;
import ru.mail.polis.collections.task.T13_RBA;
import ru.mail.polis.collections.task.T14_TBAR;
import ru.mail.polis.collections.task.T15_OHT;
import ru.mail.polis.collections.task.T16_OHTIR;
import ru.mail.polis.collections.task.T1_ADS;
import ru.mail.polis.collections.task.T2_ADSIR;
import ru.mail.polis.collections.task.T3_ADF;
import ru.mail.polis.collections.task.T4_LDS;
import ru.mail.polis.collections.task.T5_LDSIR;
import ru.mail.polis.collections.task.T6_LDF;
import ru.mail.polis.collections.task.T7_APQS;
import ru.mail.polis.collections.task.T8_APQSIR;
import ru.mail.polis.collections.task.T9_MPII;

/*
 * Created by Nechaev Mikhail
 * Since 16/12/2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        T1_ADS.class,
        T2_ADSIR.class,
        T3_ADF.class,
        T4_LDS.class,
        T5_LDSIR.class,
        T6_LDF.class,
        T7_APQS.class,
        T8_APQSIR.class,
        T9_MPII.class,
        T10_AVL.class,
        T11_AVLI.class,
        T12_AVLIR.class,
        T13_RBA.class,
        T14_TBAR.class,
        T15_OHT.class,
        T16_OHTIR.class,
})
public class TestAllTask {
    //Run this from IntelliJ IDEA
}
