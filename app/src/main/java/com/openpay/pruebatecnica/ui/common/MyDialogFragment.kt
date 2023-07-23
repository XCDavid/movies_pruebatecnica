package com.openpay.pruebatecnica.ui.common

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.openpay.pruebatecnica.R
import com.openpay.pruebatecnica.databinding.FragmentMyDialogBinding
import com.openpay.pruebatecnica.util.Constants

class MyDialogFragment : DialogFragment() {

    private var message: String? = null
    private var _binding: FragmentMyDialogBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private val closeDialogRunnable = Runnable {
        dismiss() // Cerrar el FragmentDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establecer el estilo del diálogo para tener un fondo transparente
        setStyle(STYLE_NORMAL, R.style.TransparentDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
//        val binding: FragmentMyDialogBinding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_my_dialog, container, false
//        )
        _binding = FragmentMyDialogBinding.inflate(inflater, container, false)
        arguments?.let {
            val messageIn = it.getString(Constants.INTENT_FRAGMENT_MESSAGE)
            if (!messageIn.isNullOrEmpty()) {
                message = messageIn
                binding.errorTv.text = message
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Programar el cierre del FragmentDialog después de 2 segundos
        handler.postDelayed(closeDialogRunnable, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Eliminar cualquier callback pendiente para evitar pérdidas de memoria
        handler.removeCallbacks(closeDialogRunnable)
    }

}