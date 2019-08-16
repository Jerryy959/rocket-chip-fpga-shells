// See LICENSE for license details.
package sifive.fpgashells.shell.xilinx

import chisel3._
import freechips.rocketchip.diplomacy._
import sifive.fpgashells.shell._
import sifive.fpgashells.ip.xilinx._

abstract class UARTXilinxOverlay(params: UARTOverlayParams, flowControl: Boolean)
  extends UARTOverlay(params, flowControl)
{
  def shell: XilinxShell

  InModuleBody {
    val (io, _) = uartSource.out(0)
    val tluartport = tluartSink.bundle
    io <> tluartport
    tluartport.rxd := RegNext(RegNext(io.rxd))
  }

  shell { InModuleBody {
    UIntToAnalog(uartSink.bundle.txd, io.txd, true.B)
    uartSink.bundle.rxd := AnalogToUInt(io.rxd)
  } }
}
