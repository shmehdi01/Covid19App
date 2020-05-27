package app.shmehdi.covid19app.ui.dialog

import android.content.Context
import android.widget.EditText
import app.shmehdi.covid19app.R
import app.shmehdi.covid19app.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.filter_dialog.*

class FilterDialog(
    context: Context,
    private val onFilterApply: (Int, Int, Int,Int,Int,Int) -> Unit
) : BottomSheetDialog(context) {

    private var filterConfirmMin: Int = -1
    private var filterConfirmMax: Int = -1
    private var filterDeathMin: Int = -1
    private var filterDeathMax: Int = -1
    private var filterRecoveredMin: Int = -1
    private var filterRecoveredMax: Int = -1

    init {
        setContentView(R.layout.filter_dialog)

        btn_apply.setOnClickListener {


            filterConfirmMin = et_total_case_min.parseInt()
            filterConfirmMax = et_total_case_max.parseInt()

            filterRecoveredMin = et_recovered_min.parseInt()
            filterRecoveredMax = et_recovered_max.parseInt()

            filterDeathMin = et_death_min.parseInt()
            filterDeathMax = et_death_max.parseInt()

            if ((filterConfirmMin >= 0 && filterConfirmMax >= 0) || (filterRecoveredMin >= 0 && filterRecoveredMax >= 0) || (filterDeathMin >= 0 && filterDeathMax >= 0)) {
                if(filterConfirmMin > filterConfirmMax || filterRecoveredMin > filterRecoveredMax || filterDeathMin > filterDeathMax) {
                    context.showToast("Minimum cannot be greater than maximum")
                    return@setOnClickListener
                }
                onFilterApply(filterConfirmMin,filterConfirmMax, filterRecoveredMin, filterRecoveredMax, filterDeathMin,filterDeathMax)
                dismiss()
            } else context.showToast("Enter at least one field min max set")
        }
    }

    fun setOnReset(onReset: () -> Unit) {
        tv_reset.setOnClickListener {
            onReset()
            dismiss()
            et_total_case_min.setText("")
            et_total_case_max.setText("")
            et_recovered_min.setText("")
            et_recovered_max.setText("")
            et_death_min.setText("")
            et_death_max.setText("")

            filterConfirmMin = -1
            filterConfirmMax = -1
            filterRecoveredMin = -1
            filterRecoveredMax = -1
            filterDeathMin = -1
            filterDeathMax = -1
        }
    }


    private fun EditText.parseInt(): Int {
        return try {
            text.toString().toInt()
        } catch (e: Exception) {
            -1
        }
    }
}