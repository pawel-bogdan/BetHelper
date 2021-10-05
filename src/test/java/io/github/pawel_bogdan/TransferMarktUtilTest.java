package io.github.pawel_bogdan;

import io.github.pawel_bogdan.downloaders.TransfermarktUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

public class TransferMarktUtilTest {
    @Test
    public void test_downloadSquadValue_nullTeam_returnsEmptyOptional() {
        var result = TransfermarktUtil.downloadSquadValue(null);
        Assert.assertEquals(result, Optional.empty());
    }

    @Test
    public void test_downloadSquadValue_existingTeam_returnsRealValue() {
        var result = TransfermarktUtil.downloadSquadValue("Brazylia");
        Assert.assertEquals(result, Optional.of("972,40 mln €"));
    }

    @Test
    public void test_downloadSquadValue_nonExistingTeam_returnsEmptyOptional() {
        var result = TransfermarktUtil.downloadSquadValue("non existing");
        Assert.assertEquals(result, Optional.empty());
    }
}
