package com.google.fhirengine.index.impl;

import android.os.Build;

import com.google.common.truth.Truth;
import com.google.fhirengine.index.ResourceIndices;
import com.google.fhirengine.index.StringIndex;
import com.google.fhirengine.resource.ResourceModule;

import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;

import static org.junit.Assert.assertEquals;

/** Unit tests for {@link FhirIndexerImpl}. */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class FhirIndexerImplTest {
  private static final String TEST_PATIENT_1_ID = "test_patient_1";
  private static final Patient TEST_PATIENT_1;

  static {
    TEST_PATIENT_1 = new Patient();
    TEST_PATIENT_1.setId(TEST_PATIENT_1_ID);
    TEST_PATIENT_1.addName(new HumanName().addGiven("Tom"));
  }

  @Inject
  FhirIndexerImpl fhirIndexer;

  @Singleton
  @Component(modules = {FhirIndexerModule.class, ResourceModule.class})
  public interface TestComponent {
    void inject(com.google.fhirengine.index.impl.FhirIndexerImplTest fhirIndexerImplTest);
  }

  @Before
  public void setUp() throws Exception {
    DaggerFhirIndexerImplTest_TestComponent.builder().build().inject(this);
  }

  @Test
  public void index_patient_shouldIndexGivenName() throws Exception {
    ResourceIndices resourceIndices = fhirIndexer.index(TEST_PATIENT_1);
    Truth.assertThat(resourceIndices.getStringIndices())
        .contains(StringIndex.create("given", "Patient.name.given", "Tom"));
  }

  // TODO: improve the tests.
}