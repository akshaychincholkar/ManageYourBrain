package com.manageyourbrain.domain;

import static com.manageyourbrain.domain.MemoryTestSamples.*;
import static com.manageyourbrain.domain.MnemonicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.manageyourbrain.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MnemonicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mnemonic.class);
        Mnemonic mnemonic1 = getMnemonicSample1();
        Mnemonic mnemonic2 = new Mnemonic();
        assertThat(mnemonic1).isNotEqualTo(mnemonic2);

        mnemonic2.setId(mnemonic1.getId());
        assertThat(mnemonic1).isEqualTo(mnemonic2);

        mnemonic2 = getMnemonicSample2();
        assertThat(mnemonic1).isNotEqualTo(mnemonic2);
    }

    @Test
    void memoryTest() throws Exception {
        Mnemonic mnemonic = getMnemonicRandomSampleGenerator();
        Memory memoryBack = getMemoryRandomSampleGenerator();

        mnemonic.addMemory(memoryBack);
        assertThat(mnemonic.getMemories()).containsOnly(memoryBack);
        assertThat(memoryBack.getMnemonic()).isEqualTo(mnemonic);

        mnemonic.removeMemory(memoryBack);
        assertThat(mnemonic.getMemories()).doesNotContain(memoryBack);
        assertThat(memoryBack.getMnemonic()).isNull();

        mnemonic.memories(new HashSet<>(Set.of(memoryBack)));
        assertThat(mnemonic.getMemories()).containsOnly(memoryBack);
        assertThat(memoryBack.getMnemonic()).isEqualTo(mnemonic);

        mnemonic.setMemories(new HashSet<>());
        assertThat(mnemonic.getMemories()).doesNotContain(memoryBack);
        assertThat(memoryBack.getMnemonic()).isNull();
    }
}
