//
//  TimerServiceImpl.swift
//  iosApp
//
//  Created by Victor Varenik on 31.10.2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
//
//class TimerServiceImpl : TimerService {
//    private let sysTimer = Timer()
//    private var timer: Timer? = nil
//    private var state: TimerState = .STOPPED
//    private var stage: TimerStage = .WORK
//    private var currentSeconds: Int = 0
//    
//    private var listener: TimeListener? = nil
//    
//    private func onTimerTick() {
//        if(state == .STARTED && currentSeconds > 0) {
//            currentSeconds -= 1
//            
//            let timeName = stage == .work ? "Work time" : "Break time"
//            
//            notifyTimeChanged()
//            
//            if currentSeconds == 0 {
//                stop()
//            }
//        }
//    }
//    
//    private func notifyStateChanged() {
//        if let timer = timer {
//            listener?.onStateUpdated(timer, state)
//        }
//    }
//    
//    private func notifyStageChanged() {
//        if let timer = timer {
//            listener?.onStageUpdated(timer, stage)
//        }
//    }
//    
//    private func notifyTimeChanged() {
//        if let timer = timer {
//            listener?.onTimeUpdated(timer, currentSeconds)
//        }
//    }
//    
//    private func resetTime() {
//        let currentStartTime = (stage == .work) ? timer?.workTime : timer?.shortBreakTime
//        currentSeconds = Int(currentStartTime ?? 0)
//        notifyTimeChanged()
//    }
//    
//    func stop() {
//        state = .stopped
//        notifyStateChanged()
//    }
//    
//    override func start() {
//        resetTime()
//        state = .started
//        notifyStateChanged()
//    }
//    
//    override func pause() {
//        state = .paused
//        notifyStateChanged()
//    }
//    
//    override func resume() {
//        state = .started
//        notifyStateChanged()
//    }
//    
//    override func setTimer(_ timer: Timer?) {
//        self.timer = timer
//        
//        if let timer = timer {
//            // Notify all updated data
//            notifyStageChanged()
//            notifyStateChanged()
//            resetTime()
//        }
//    }
//    
//    override func setStage(_ stage: TimerStage) {
//        if state != .stopped {
//            stop()
//        }
//        
//        self.stage = stage
//        resetTime()
//        notifyStageChanged()
//    }
//    
//    override func setListener(_ listener: TimerListener?) {
//        self.listener = listener
//    }
//    
//    override func getTimer() -> Timer? {
//        return timer
//    }
//}
