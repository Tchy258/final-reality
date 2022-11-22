package cl.uchile.dcc.finalreality.model

import io.kotest.property.Arb

/**
 * This interface encapsulates generators for testing data
 * (this data is supplied to testing factories)
 */
interface ModelData {
    /**
     * An arb that creates valid model data
     */
    val validGenerator: Arb<ModelData>

    /**
     * An arb that creates arbitrary model data
     */
    val arbitraryGenerator: Arb<ModelData>
}
